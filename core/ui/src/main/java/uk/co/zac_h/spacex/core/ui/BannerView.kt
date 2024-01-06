package uk.co.zac_h.spacex.core.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.view.isVisible
import uk.co.zac_h.spacex.core.ui.databinding.BannerViewBinding

class BannerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val binding: BannerViewBinding =
        BannerViewBinding.inflate(LayoutInflater.from(context), this, true)

    var message: String? = null
        set(value) {
            field = value

            binding.message.text = value
        }

    var primaryActionLabel: String? = null
        set(value) {
            field = value

            binding.primaryAction.text = value
            binding.primaryAction.isVisible = value != null
        }

    var secondaryActionLabel: String? = null
        set(value) {
            field = value

            binding.secondaryAction.text = value
            binding.secondaryAction.isVisible = value != null
        }

    fun setPrimaryOnClick(listener: OnClickListener) {
        binding.primaryAction.setOnClickListener(listener)
    }

    fun setSecondaryOnClickListener(listener: OnClickListener) {
        binding.secondaryAction.setOnClickListener(listener)
    }

    init {
        visibility = View.GONE

        context.obtainStyledAttributes(attrs, R.styleable.BannerView).use {
            message = it.getString(R.styleable.BannerView_message)
            primaryActionLabel = it.getString(R.styleable.BannerView_primaryActionLabel)
            secondaryActionLabel = it.getString(R.styleable.BannerView_secondaryActionLabel)
        }
    }

    fun setPrimaryAsDismiss() {
        setPrimaryOnClick { visibility = View.GONE }
    }

    fun setSecondaryAsDismiss() {
        setSecondaryOnClickListener { visibility = View.GONE }
    }
}

@Composable
fun Banner(
    modifier: Modifier = Modifier,
    message: String,
    primaryActionLabel: String? = null,
    secondaryActionLabel: String? = null,
    primaryAction: (() -> Unit)? = null,
    secondaryAction: (() -> Unit)? = null,
    primaryActionAsDismiss: Boolean = false,
    secondaryActionAsDismiss: Boolean = false,
    isVisible: Boolean = false
) {
    var isVisibleInternal by rememberSaveable {
        mutableStateOf(isVisible)
    }

    AnimatedVisibility(visible = isVisibleInternal) {
        Column(
            modifier = modifier
                .background(MaterialTheme.colorScheme.background)
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .padding(horizontal = 16.dp),
                text = message,
                color = MaterialTheme.colorScheme.onBackground
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    modifier = Modifier
                        .padding(end = 8.dp, top = 8.dp),
                    onClick = {
                        if (secondaryActionAsDismiss) {
                            isVisibleInternal = false
                        } else {
                            secondaryAction?.invoke()
                        }
                    }
                ) {
                    secondaryActionLabel?.let { Text(text = it) }
                }
                TextButton(
                    modifier = Modifier
                        .padding(end = 8.dp, top = 8.dp),
                    onClick = {
                        if (primaryActionAsDismiss) {
                            isVisibleInternal = false
                        } else {
                            primaryAction?.invoke()
                        }
                    }
                ) {
                    primaryActionLabel?.let { Text(text = it) }
                }
            }
            Divider()
        }
    }
}

@ComponentPreviews
@Composable
fun BannerPreview() {
    SpaceXTheme {
        Banner(
            message = "Error Message",
            primaryActionLabel = "Primary",
            secondaryActionLabel = "Secondary",
            isVisible = true
        )
    }
}