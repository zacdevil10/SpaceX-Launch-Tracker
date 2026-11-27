package uk.co.zac_h.spacex.feature.starlink.orbit

import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Mean orbital elements as published in OMM / GP data (the same numbers as a TLE).
 *
 * @param epochMillis epoch of the elements, in milliseconds since the Unix epoch (UTC).
 * @param meanMotion revolutions per day.
 * @param inclination, [raan], [argOfPerigee], [meanAnomaly] in degrees.
 * @param bstar drag term, in inverse Earth radii.
 */
data class OrbitalElements(
    val epochMillis: Long,
    val meanMotion: Double,
    val eccentricity: Double,
    val inclination: Double,
    val raan: Double,
    val argOfPerigee: Double,
    val meanAnomaly: Double,
    val bstar: Double
)

/**
 * The SGP4 propagator for near-Earth orbits (period under 225 minutes, which covers every Starlink
 * satellite), with WGS-72 constants and Vallado's "improved" mode, following Vallado et al.,
 * "Revisiting Spacetrack Report #3" (AIAA 2006-6753). Deep-space (SDP4) orbits are rejected.
 *
 * Everything that depends only on the elements is worked out once here, so [propagate] is cheap
 * enough to run for ten thousand satellites several times a second.
 */
class Sgp4(elements: OrbitalElements) {

    val epochMillis = elements.epochMillis

    private val ecco = elements.eccentricity
    private val inclo = Math.toRadians(elements.inclination)
    private val nodeo = Math.toRadians(elements.raan)
    private val argpo = Math.toRadians(elements.argOfPerigee)
    private val mo = Math.toRadians(elements.meanAnomaly)
    private val bstar = elements.bstar

    /** Un-Kozai'd mean motion, radians per minute. */
    private val no: Double
    private val isimp: Boolean
    private val con41: Double
    private val x1mth2: Double
    private val x7thm1: Double
    private val cc1: Double
    private val cc4: Double
    private val cc5: Double
    private val d2: Double
    private val d3: Double
    private val d4: Double
    private val delmo: Double
    private val eta: Double
    private val argpdot: Double
    private val omgcof: Double
    private val sinmao: Double
    private val t2cof: Double
    private val t3cof: Double
    private val t4cof: Double
    private val t5cof: Double
    private val xlcof: Double
    private val aycof: Double
    private val xmcof: Double
    private val mdot: Double
    private val nodecf: Double
    private val nodedot: Double
    private val cosio: Double
    private val sinio: Double

    init {
        // initl: recover the original mean motion and semi-major axis from the Kozai mean motion.
        val noKozai = elements.meanMotion * TWO_PI / MINUTES_PER_DAY
        val eccsq = ecco * ecco
        val omeosq = 1.0 - eccsq
        val rteosq = sqrt(omeosq)
        cosio = cos(inclo)
        val cosio2 = cosio * cosio

        val ak = (XKE / noKozai).pow(X2O3)
        val d1 = 0.75 * J2 * (3.0 * cosio2 - 1.0) / (rteosq * omeosq)
        var del = d1 / (ak * ak)
        val adel = ak * (1.0 - del * del - del * (1.0 / 3.0 + 134.0 * del * del / 81.0))
        del = d1 / (adel * adel)
        no = noKozai / (1.0 + del)

        val ao = (XKE / no).pow(X2O3)
        sinio = sin(inclo)
        val po = ao * omeosq
        val con42 = 1.0 - 5.0 * cosio2
        con41 = -con42 - cosio2 - cosio2
        val posq = po * po
        val rp = ao * (1.0 - ecco)

        require(TWO_PI / no < 225.0) { "Deep-space orbits need SDP4, which is not implemented" }

        // sgp4init
        isimp = rp < 220.0 / RADIUS_EARTH_KM + 1.0

        var sfour = 78.0 / RADIUS_EARTH_KM + 1.0
        var qzms24 = ((120.0 - 78.0) / RADIUS_EARTH_KM).pow(4)
        val perige = (rp - 1.0) * RADIUS_EARTH_KM
        if (perige < 156.0) {
            sfour = if (perige < 98.0) 20.0 else perige - 78.0
            qzms24 = ((120.0 - sfour) / RADIUS_EARTH_KM).pow(4)
            sfour = sfour / RADIUS_EARTH_KM + 1.0
        }
        val pinvsq = 1.0 / posq

        val tsi = 1.0 / (ao - sfour)
        eta = ao * ecco * tsi
        val etasq = eta * eta
        val eeta = ecco * eta
        val psisq = abs(1.0 - etasq)
        val coef = qzms24 * tsi.pow(4)
        val coef1 = coef / psisq.pow(3.5)
        val cc2 = coef1 * no * (ao * (1.0 + 1.5 * etasq + eeta * (4.0 + etasq)) +
            0.375 * J2 * tsi / psisq * con41 * (8.0 + 3.0 * etasq * (8.0 + etasq)))
        cc1 = bstar * cc2
        val cc3 = if (ecco > 1.0e-4) -2.0 * coef * tsi * J3OJ2 * no * sinio / ecco else 0.0
        x1mth2 = 1.0 - cosio2
        cc4 = 2.0 * no * coef1 * ao * omeosq * (eta * (2.0 + 0.5 * etasq) + ecco * (0.5 + 2.0 * etasq) -
            J2 * tsi / (ao * psisq) * (-3.0 * con41 * (1.0 - 2.0 * eeta + etasq * (1.5 - 0.5 * eeta)) +
                0.75 * x1mth2 * (2.0 * etasq - eeta * (1.0 + etasq)) * cos(2.0 * argpo)))
        cc5 = 2.0 * coef1 * ao * omeosq * (1.0 + 2.75 * (etasq + eeta) + eeta * etasq)

        val cosio4 = cosio2 * cosio2
        val temp1 = 1.5 * J2 * pinvsq * no
        val temp2 = 0.5 * temp1 * J2 * pinvsq
        val temp3 = -0.46875 * J4 * pinvsq * pinvsq * no
        mdot = no + 0.5 * temp1 * rteosq * con41 +
            0.0625 * temp2 * rteosq * (13.0 - 78.0 * cosio2 + 137.0 * cosio4)
        argpdot = -0.5 * temp1 * con42 + 0.0625 * temp2 * (7.0 - 114.0 * cosio2 + 395.0 * cosio4) +
            temp3 * (3.0 - 36.0 * cosio2 + 49.0 * cosio4)
        val xhdot1 = -temp1 * cosio
        nodedot = xhdot1 + (0.5 * temp2 * (4.0 - 19.0 * cosio2) + 2.0 * temp3 * (3.0 - 7.0 * cosio2)) * cosio
        omgcof = bstar * cc3 * cos(argpo)
        xmcof = if (ecco > 1.0e-4) -X2O3 * coef * bstar / eeta else 0.0
        nodecf = 3.5 * omeosq * xhdot1 * cc1
        t2cof = 1.5 * cc1
        xlcof = if (abs(cosio + 1.0) > 1.5e-12) {
            -0.25 * J3OJ2 * sinio * (3.0 + 5.0 * cosio) / (1.0 + cosio)
        } else {
            -0.25 * J3OJ2 * sinio * (3.0 + 5.0 * cosio) / 1.5e-12
        }
        aycof = -0.5 * J3OJ2 * sinio
        delmo = (1.0 + eta * cos(mo)).pow(3)
        sinmao = sin(mo)
        x7thm1 = 7.0 * cosio2 - 1.0

        if (!isimp) {
            val cc1sq = cc1 * cc1
            d2 = 4.0 * ao * tsi * cc1sq
            val temp = d2 * tsi * cc1 / 3.0
            d3 = (17.0 * ao + sfour) * temp
            d4 = 0.5 * temp * ao * tsi * (221.0 * ao + 31.0 * sfour) * cc1
            t3cof = d2 + 2.0 * cc1sq
            t4cof = 0.25 * (3.0 * d3 + cc1 * (12.0 * d2 + 10.0 * cc1sq))
            t5cof = 0.2 * (3.0 * d4 + 12.0 * cc1 * d3 + 6.0 * d2 * d2 + 15.0 * cc1sq * (2.0 * d2 + cc1sq))
        } else {
            d2 = 0.0; d3 = 0.0; d4 = 0.0; t3cof = 0.0; t4cof = 0.0; t5cof = 0.0
        }
    }

    /**
     * Position (km) and velocity (km/s) in the TEME frame, [minutesSinceEpoch] after the epoch,
     * written to [state] as x, y, z, vx, vy, vz. Returns false when there is no valid orbit.
     */
    fun propagate(minutesSinceEpoch: Double, state: DoubleArray): Boolean {
        val t = minutesSinceEpoch

        // Secular gravity and atmospheric drag.
        val xmdf = mo + mdot * t
        val argpdf = argpo + argpdot * t
        val nodedf = nodeo + nodedot * t
        var argpm = argpdf
        var mm = xmdf
        val t2 = t * t
        var nodem = nodedf + nodecf * t2
        var tempa = 1.0 - cc1 * t
        var tempe = bstar * cc4 * t
        var templ = t2cof * t2

        if (!isimp) {
            val delomg = omgcof * t
            val delm = xmcof * ((1.0 + eta * cos(xmdf)).pow(3) - delmo)
            val temp = delomg + delm
            mm = xmdf + temp
            argpm = argpdf - temp
            val t3 = t2 * t
            val t4 = t3 * t
            tempa = tempa - d2 * t2 - d3 * t3 - d4 * t4
            tempe += bstar * cc5 * (sin(mm) - sinmao)
            templ += t3cof * t3 + t4 * (t4cof + t * t5cof)
        }

        val am = (XKE / no).pow(X2O3) * tempa * tempa
        val nm = XKE / am.pow(1.5)
        var em = ecco - tempe
        if (em >= 1.0 || em < -0.001 || am < 0.95) return false
        if (em < 1.0e-6) em = 1.0e-6
        mm += no * templ
        var xlm = mm + argpm + nodem
        nodem = mod2pi(nodem)
        argpm = mod2pi(argpm)
        xlm = mod2pi(xlm)
        mm = mod2pi(xlm - argpm - nodem)

        // Long-period periodics.
        val axnl = em * cos(argpm)
        var temp = 1.0 / (am * (1.0 - em * em))
        val aynl = em * sin(argpm) + temp * aycof
        val xl = mm + argpm + nodem + temp * xlcof * axnl

        // Kepler's equation.
        val u = mod2pi(xl - nodem)
        var eo1 = u
        var tem5 = 9999.9
        var ktr = 1
        var sineo1 = 0.0
        var coseo1 = 0.0
        while (abs(tem5) >= 1.0e-12 && ktr <= 10) {
            sineo1 = sin(eo1)
            coseo1 = cos(eo1)
            tem5 = 1.0 - coseo1 * axnl - sineo1 * aynl
            tem5 = (u - aynl * coseo1 + axnl * sineo1 - eo1) / tem5
            if (abs(tem5) >= 0.95) tem5 = if (tem5 > 0.0) 0.95 else -0.95
            eo1 += tem5
            ktr++
        }

        // Short-period periodics.
        val ecose = axnl * coseo1 + aynl * sineo1
        val esine = axnl * sineo1 - aynl * coseo1
        val el2 = axnl * axnl + aynl * aynl
        val pl = am * (1.0 - el2)
        if (pl < 0.0) return false
        val rl = am * (1.0 - ecose)
        val rdotl = sqrt(am) * esine / rl
        val rvdotl = sqrt(pl) / rl
        val betal = sqrt(1.0 - el2)
        temp = esine / (1.0 + betal)
        val sinu = am / rl * (sineo1 - aynl - axnl * temp)
        val cosu = am / rl * (coseo1 - axnl + aynl * temp)
        var su = atan2(sinu, cosu)
        val sin2u = (cosu + cosu) * sinu
        val cos2u = 1.0 - 2.0 * sinu * sinu
        temp = 1.0 / pl
        val temp1 = 0.5 * J2 * temp
        val temp2 = temp1 * temp

        val mrt = rl * (1.0 - 1.5 * temp2 * betal * con41) + 0.5 * temp1 * x1mth2 * cos2u
        if (mrt < 1.0) return false // Below the Earth's surface: decayed.
        su -= 0.25 * temp2 * x7thm1 * sin2u
        val xnode = nodem + 1.5 * temp2 * cosio * sin2u
        val xinc = inclo + 1.5 * temp2 * cosio * sinio * cos2u
        val mvt = rdotl - nm * temp1 * x1mth2 * sin2u / XKE
        val rvdot = rvdotl + nm * temp1 * (x1mth2 * cos2u + 1.5 * con41) / XKE

        // Orientation vectors.
        val sinsu = sin(su)
        val cossu = cos(su)
        val snod = sin(xnode)
        val cnod = cos(xnode)
        val sini = sin(xinc)
        val cosi = cos(xinc)
        val xmx = -snod * cosi
        val xmy = cnod * cosi
        val ux = xmx * sinsu + cnod * cossu
        val uy = xmy * sinsu + snod * cossu
        val uz = sini * sinsu
        val vx = xmx * cossu - cnod * sinsu
        val vy = xmy * cossu - snod * sinsu
        val vz = sini * cossu

        state[0] = mrt * ux * RADIUS_EARTH_KM
        state[1] = mrt * uy * RADIUS_EARTH_KM
        state[2] = mrt * uz * RADIUS_EARTH_KM
        state[3] = (mvt * ux + rvdot * vx) * VKMPERSEC
        state[4] = (mvt * uy + rvdot * vy) * VKMPERSEC
        state[5] = (mvt * uz + rvdot * vz) * VKMPERSEC
        return true
    }

    companion object {
        // WGS-72, as SGP4 element sets are fitted with these.
        const val RADIUS_EARTH_KM = 6378.135
        private const val MU = 398600.8
        private const val J2 = 0.001082616
        private const val J3 = -0.00000253881
        private const val J4 = -0.00000165597
        private const val J3OJ2 = J3 / J2
        private val XKE = 60.0 / sqrt(RADIUS_EARTH_KM * RADIUS_EARTH_KM * RADIUS_EARTH_KM / MU)
        private val VKMPERSEC = RADIUS_EARTH_KM * XKE / 60.0
        private const val X2O3 = 2.0 / 3.0
        private const val TWO_PI = 2.0 * PI
        private const val MINUTES_PER_DAY = 1440.0

        private fun mod2pi(x: Double): Double {
            val r = x % TWO_PI
            return if (r < 0.0) r + TWO_PI else r
        }
    }
}
