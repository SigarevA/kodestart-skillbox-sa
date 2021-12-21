package ru.kode.base.internship.products.ui.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.kode.base.internship.auth.ui.R
import ru.kode.base.internship.products.ui.core.uikit.theme.AppTheme

@Composable
fun Logo() {
  Icon(
    painter = painterResource(id = R.drawable.ic_logo),
    contentDescription = "KODE logo",
    tint = AppTheme.colors.contendAccentPrimary
  )

  Spacer(modifier = Modifier.height(8.dp))

  Icon(
    painter = painterResource(id = R.drawable.ic_sub_logo),
    contentDescription = "KODE sub logo",
    tint = AppTheme.colors.contendAccentTertiary
  )
}