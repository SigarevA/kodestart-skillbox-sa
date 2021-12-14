package ru.sigarev.products.ui

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.valentinilk.shimmer.shimmer
import ru.kode.base.internship.ui.core.uikit.KodeBankBaseController
import ru.kode.base.internship.ui.core.uikit.theme.AppTheme
import ru.sigarev.products.model.BankAccount
import ru.sigarev.products.model.Card
import ru.sigarev.products.model.CardStatus
import ru.sigarev.products.model.Deposit
import ru.sigarev.products.ui.ProductsMainScreen.ViewIntents
import ru.sigarev.products.utils.drawable
import ru.sigarev.products.utils.icon
import ru.sigarev.products.utils.string

internal class ProductsMainController :
  KodeBankBaseController<ProductsMainScreen.ViewState, ViewIntents>() {

  override fun createConfig(): Config<ViewIntents> {
    return object : Config<ViewIntents> {
      override val intentsConstructor = ::ViewIntents
    }
  }

  @ExperimentalComposeUiApi
  @Composable
  override fun ScreenContent(state: ProductsMainScreen.ViewState) {
    Box(
      contentAlignment = Alignment.TopCenter,
      modifier = Modifier
        .statusBarsPadding()
        .navigationBarsWithImePadding()
    ) {
      ShimmerList()
    }
    /*
    Box(
      contentAlignment = Alignment.TopCenter,
      modifier = Modifier
        .statusBarsPadding()
        .navigationBarsWithImePadding()
    ) {
      val yourShimmerTheme = defaultShimmerTheme.copy(
        shaderColors = listOf(Color(0xFF403A47).copy(alpha = 0.5f),
          Color.Unspecified.copy(alpha = 0.5f),
          Color(0xFF403A47).copy(alpha = 0.5f))
        //shaderColors = listOf(Color(0xFF403A47), Color(0xFF706D76), Color(0xFF403A47))
      )
      CompositionLocalProvider(
        LocalShimmerTheme provides yourShimmerTheme
      ) {
        LazyColumn {
          item {
            ShimmerLoading()
          }
          item {
            ScreenHeader()
          }
          if (state.bankAccountsLceState == LceState.Loading)
            item { Loading() }
          state.bankAccounts?.let { bankinfo ->
            if (bankinfo.bankAccounts.isNotEmpty()) {
              item {
                HeaderBankAccounts()
              }
              bankinfo.bankAccounts.forEachIndexed { i, bankAccount ->
                item {
                  BankAccount(bankAccount = bankAccount, i)
                }
                if (bankinfo.expandBankAccounts[i])
                  bankAccount.cards.forEach { card ->
                    item {
                      BankCard(Modifier, card)
                    }
                  }
              }
            }
          }
          item {
            Spacer(modifier = Modifier.height(11.dp))
          }
          if (state.depositsLceState == LceState.Loading)
            item { Loading() }
          state.deposits?.let {
            if (it.isNotEmpty()) {
              item {
                BankDepositHeader()
              }
              it.forEachIndexed { i, deposit ->
                item {
                  BankDeposit(deposit, i == it.size - 1 && i != 1)
                }
              }
            }
          }
          item {
            PrimaryButton(
              modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
              text = stringResource(id = R.string.products_open_new_deposit_product),
              onClick = {
              }
            )
          }
        }
      }
    }
    */
  }

  @Composable
  fun ShimmerLoading() {
    Surface(
      color = AppTheme.colors.contendTertiary,
      shape = RoundedCornerShape(32.dp),
      modifier = Modifier
        .size(64.dp)
        //.background(AppTheme.colors.backgroundSecondary)
        .shimmer()
    ) {
      Spacer(modifier = Modifier
        .width(32.dp)
        .height(32.dp))
    }
  }

  @Composable
  fun Loading() {
    Surface(
      color = AppTheme.colors.backgroundSecondary,
      modifier = Modifier.fillMaxWidth()
    ) {
      Box(modifier = Modifier
        .fillMaxWidth()
        .padding(16.dp),
        contentAlignment = Alignment.Center
      ) {
        Column {
          CircularProgressIndicator(
            modifier = Modifier
              .padding(end = 16.dp),
            color = AppTheme.colors.contendAccentPrimary,
            strokeWidth = 1.5.dp
          )
        }
        ShimmerLoading()
      }
    }
  }

  @Composable
  fun ScreenHeader() {
    Column {
      Spacer(modifier = Modifier.height(14.dp))
      Text(
        text = stringResource(id = R.string.products_main),
        color = AppTheme.colors.textPrimary,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        style = AppTheme.typography.bodyMedium
      )
      Spacer(modifier = Modifier.height(14.dp))
    }
  }

  @Composable
  fun BankCard(
    modifier: Modifier,
    card: Card,
  ) {
    Surface(
      color = AppTheme.colors.backgroundSecondary,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(modifier = modifier
        .fillMaxWidth()
        .clickable {

        },
        verticalAlignment = Alignment.CenterVertically) {
        Icon(
          painter = painterResource(id = R.drawable.ic_forward_arrow),
          contentDescription = stringResource(id = R.string.products_detail),
          tint = AppTheme.colors.textTertiary,
          modifier = Modifier.padding(horizontal = 30.dp)
        )

        Column(modifier = Modifier
          .padding(end = 16.dp)
          .weight(1f)) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
              Spacer(modifier = Modifier.height(16.dp))
              Text(stringResource(id = card.cardType.string),
                color = AppTheme.colors.contendAccentTertiary,
                style = AppTheme.typography.body2)
              Spacer(modifier = Modifier.height(3.dp))
              Text(stringResource(id = card.status.string),
                color = if (card.status == CardStatus.Locked) AppTheme.colors.indicatorContendError else AppTheme.colors.textSecondary,
                style = AppTheme.typography.caption1)
              Spacer(modifier = Modifier.height(14.dp))
            }
            Box(contentAlignment = Alignment.CenterEnd) {
              Image(
                painter = painterResource(id = R.drawable.bg_card),
                contentDescription = null,
              )
              Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(end = 4.dp)
              ) {
                Text(card.shortNumber, color = AppTheme.colors.contendAccentTertiary,
                  style = AppTheme.typography.caption2
                )
                Image(painter = painterResource(id = card.paymentSystem.drawable),
                  contentDescription = card.paymentSystem.name)
              }
            }
          }
          Surface(modifier = Modifier.height(1.dp), color = AppTheme.colors.contendSecondary) {}
        }
      }
    }
  }

  @Composable
  fun BankAccount(bankAccount: BankAccount, pos: Int) {
    Surface(
      color = AppTheme.colors.backgroundSecondary,
      modifier = Modifier
        .fillMaxWidth()
        .clickable {

        }
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
          painter = painterResource(id = bankAccount.currency.icon),
          contentDescription = "Знак монеты",
          modifier = Modifier.padding(16.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
          Spacer(modifier = Modifier.height(16.dp))
          Text("Счет расчетный", color = AppTheme.colors.contendAccentTertiary, style = AppTheme.typography.body2)
          Spacer(modifier = Modifier.height(2.dp))
          Text(bankAccount.sym, color = AppTheme.colors.contendAccentPrimary, style = AppTheme.typography.body2)
          Spacer(modifier = Modifier.height(14.dp))
        }
        Surface(
          modifier = Modifier
            .padding(16.dp)
            .clickable {
              intents.clickExpand(pos)
            },
          shape = RoundedCornerShape(3.dp),
          color = AppTheme.colors.contendSecondary
        ) {
          Icon(painter = painterResource(id = R.drawable.ic_expand),
            contentDescription = "Знак монеты",
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 11.dp)
          )
        }
      }
    }
  }

  @Composable
  fun HeaderBankAccounts() {
    Surface(
      color = AppTheme.colors.backgroundSecondary,
      modifier = Modifier.fillMaxWidth()
    ) {
      Text(
        text = stringResource(id = R.string.products_accounts),
        color = AppTheme.colors.textTertiary,
        modifier = Modifier.padding(16.dp)
      )
    }
  }

  @Composable
  fun BankDepositHeader() {
    Surface(
      color = AppTheme.colors.backgroundSecondary,
      modifier = Modifier
        .fillMaxWidth()
        .padding(top = 16.dp)
    ) {
      Text(
        text = stringResource(id = R.string.products_deposits),
        color = AppTheme.colors.textTertiary,
        modifier = Modifier.padding(16.dp)
      )
    }
  }

  @Composable
  fun BankDeposit(
    deposit: Deposit,
    isLast: Boolean = false,
  ) {
    Surface(
      color = AppTheme.colors.backgroundSecondary,
      modifier = Modifier
        .fillMaxWidth()
        .clickable {
          intents.navigateDeposit(deposit)
        }
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Image(painter = painterResource(id = deposit.icon),
          contentDescription = null,
          modifier = Modifier.padding(16.dp))
        Column(modifier = Modifier
          .weight(1f)
          .padding(end = 16.dp)) {
          Spacer(modifier = Modifier.height(14.dp))
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(deposit.name,
              color = AppTheme.colors.textPrimary,
              modifier = Modifier.weight(1f),
              style = AppTheme.typography.body2)
            Text(deposit.bid, color = AppTheme.colors.textTertiary, style = AppTheme.typography.caption2)
          }
          Spacer(modifier = Modifier.height(4.dp))
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(deposit.sum, color = AppTheme.colors.contendAccentPrimary, modifier = Modifier.weight(1f),
              style = AppTheme.typography.body2
            )
            Text(deposit.validityUpTo, color = AppTheme.colors.textTertiary, style = AppTheme.typography.caption2)
          }
          Spacer(modifier = Modifier.height(14.dp))
          if (!isLast)
            Spacer(modifier = Modifier
              .height(1.dp)
              .fillMaxWidth()
              .background(color = AppTheme.colors.contendSecondary)
            )
        }
      }
    }
  }
}

enum class ShimmerAnimationType {
  FADE, TRANSLATE, FADETRANSLATE, VERTICAL
}

@Preview
@Composable
fun ShimmerList() {
  var shimmerAnimationType by remember { mutableStateOf(ShimmerAnimationType.FADE) }
  with(LocalDensity.current) {
    4.dp.toPx()
  }
  val transition = rememberInfiniteTransition()
  val translateAnim by transition.animateFloat(
    initialValue = 300f,
    targetValue = 400f,
    animationSpec = infiniteRepeatable(
      tween(durationMillis = 1200, easing = LinearEasing),
      RepeatMode.Restart
    )
  )

  val colorAnim by transition.animateColor(
    initialValue = Color.LightGray.copy(alpha = 0.6f),
    targetValue = Color.LightGray,
    animationSpec = infiniteRepeatable(
      tween(durationMillis = 1200, easing = FastOutSlowInEasing),
      RepeatMode.Restart
    )
  )

  val list = if (shimmerAnimationType != ShimmerAnimationType.TRANSLATE) {
    listOf(colorAnim, colorAnim.copy(alpha = 0.5f))
  } else {
    listOf(
      Color(0xFF403A47),
      AppTheme.colors.backgroundPrimary,
      Color(0xFF403A47)
    )
  }

  val dpValue = if (shimmerAnimationType != ShimmerAnimationType.FADE) {
    translateAnim.dp
  } else {
    2000.dp
  }

  /*
    containerColor = if (shimmerAnimationType == type)
      MaterialTheme.colorScheme.primary else Color.LightGray
   */

  Column(
    modifier = Modifier
      .fillMaxSize()
      .verticalScroll(rememberScrollState())
  ) {

    val transition2 = rememberInfiniteTransition()
    val translateAnim2 by transition2.animateFloat(
      initialValue = with(LocalDensity.current) {
        -140.dp.toPx()
      },
      targetValue = with(LocalDensity.current) {
        140.dp.toPx()
      },
      animationSpec = infiniteRepeatable(
        tween(durationMillis = 1200, easing = LinearEasing),
        RepeatMode.Restart
      )
    )
    val translateAnim3 by transition2.animateFloat(
      initialValue = with(LocalDensity.current) {
        -20.dp.toPx()
      },
      targetValue = with(LocalDensity.current) {
        260.dp.toPx()
      },
      animationSpec = infiniteRepeatable(
        tween(durationMillis = 1200, easing = LinearEasing),
        RepeatMode.Restart
      )
    )

    val brush = Brush.horizontalGradient(
      0.0f to AppTheme.colors.contendTertiary.copy(0.5f),
      0.1f to Color(0xFF403A47).copy(0.5f),
      0.5f to AppTheme.colors.contendTertiary.copy(0.5f),
      0.99f to Color(0xFF403A47).copy(0.5f),
      1f to AppTheme.colors.contendTertiary.copy(0.5f),
      startX = translateAnim2,
      endX = translateAnim3
    )

    val col = arrayOf(
      0.0f to AppTheme.colors.contendTertiary.copy(0.5f),
      0.1f to Color(0xFF403A47).copy(0.5f),
      0.5f to AppTheme.colors.contendTertiary.copy(0.5f),
      0.99f to Color(0xFF403A47).copy(0.5f),
      1f to AppTheme.colors.contendTertiary.copy(0.5f),
    )

    val brush5 = Brush.horizontalGradient(
      *col,
      startX = translateAnim2,
      endX = translateAnim3
    )


    Row {
      Spacer(modifier = Modifier
        .padding(16.dp)
        .size(40.dp)
        .background(brush, shape = RoundedCornerShape(20.dp))
      )
      BoxWithConstraints(
        modifier = Modifier.weight(1f)
      ) {
        val translateAnim4 by transition2.animateFloat(
          initialValue = with(LocalDensity.current) {
            (0.dp - maxWidth - 20.dp).toPx()
          },
          targetValue = with(LocalDensity.current) {
            (maxWidth + 20.dp).toPx()
          },
          animationSpec = infiniteRepeatable(
            tween(durationMillis = 1200, easing = LinearEasing),
            RepeatMode.Restart
          )
        )

        val translateAnim5 by transition2.animateFloat(
          initialValue = with(LocalDensity.current) {
            -20.dp.toPx()
          },
          targetValue = with(LocalDensity.current) {
            (maxWidth + maxWidth + 20.dp).toPx()
          },
          animationSpec = infiniteRepeatable(
            tween(durationMillis = 1200, easing = LinearEasing),
            RepeatMode.Restart
          )
        )

        val br = Brush.horizontalGradient(
          *col,
          startX = translateAnim4,
          endX = translateAnim5
        )

        Column {
          Spacer(modifier = Modifier.height(16.dp))
          Spacer(modifier = Modifier
            .height(16.dp)
            .fillMaxSize()
            .size(40.dp)
            .background(br, shape = RoundedCornerShape(12.dp))
          )
        }
      }
      Spacer(modifier = Modifier
        .width(16.dp)
      )
    }

    Row(
      horizontalArrangement = Arrangement.SpaceAround,
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
      Button(
        onClick = { shimmerAnimationType = ShimmerAnimationType.FADE },
        modifier = Modifier
          .width(200.dp)
          .padding(8.dp)
      ) {
        Text(text = "Fading")
      }

      Button(
        onClick = { shimmerAnimationType = ShimmerAnimationType.TRANSLATE },
        modifier = Modifier
          .width(200.dp)
          .padding(8.dp)
      ) {
        Text(text = "Translating")
      }
    }

    Row(
      horizontalArrangement = Arrangement.SpaceAround,
      modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
    ) {
      Button(
        onClick = { shimmerAnimationType = ShimmerAnimationType.FADETRANSLATE },
        modifier = Modifier
          .width(200.dp)
          .padding(8.dp)
      ) {
        Text(text = "Fade+Translate")
      }
      Button(
        onClick = { shimmerAnimationType = ShimmerAnimationType.VERTICAL },
        modifier = Modifier
          .width(200.dp)
          .padding(8.dp)
      ) {
        Text(text = "Vertical")
      }
    }

    ShimmerItem(list, dpValue.value, shimmerAnimationType == ShimmerAnimationType.VERTICAL)
    ShimmerItemBig(list, dpValue.value, shimmerAnimationType == ShimmerAnimationType.VERTICAL)
    ShimmerItem(list, dpValue.value, shimmerAnimationType == ShimmerAnimationType.VERTICAL)
    ShimmerItem(list, dpValue.value, shimmerAnimationType == ShimmerAnimationType.VERTICAL)
  }
}

@Composable
fun ShimmerItem(lists: List<Color>, floatAnim: Float = 0f, isVertical: Boolean) {
  val brush = if (isVertical) Brush.verticalGradient(lists, 0f, floatAnim) else
    Brush.horizontalGradient(lists, 0f, floatAnim)
  Row(modifier = Modifier.padding(16.dp)) {
    Spacer(
      modifier = Modifier
        .size(100.dp)
        .background(brush = brush)
    )
    Column(modifier = Modifier.padding(8.dp)) {
      Spacer(
        modifier = Modifier
          .fillMaxWidth()
          .height(30.dp)
          .padding(8.dp)
          .background(brush = brush)
      )
      Spacer(
        modifier = Modifier
          .fillMaxWidth()
          .height(30.dp)
          .padding(8.dp)
          .background(brush = brush)
      )
      Spacer(
        modifier = Modifier
          .fillMaxWidth()
          .height(30.dp)
          .padding(8.dp)
          .background(brush = brush)
      )
    }
  }
}

@Composable
fun ShimmerItemBig(lists: List<Color>, floatAnim: Float = 0f, isVertical: Boolean) {
  val brush = if (isVertical) Brush.verticalGradient(lists, 0f, floatAnim) else
    Brush.horizontalGradient(lists, 0f, floatAnim)
  Column(modifier = Modifier.padding(16.dp)) {
    Spacer(
      modifier = Modifier
        .fillMaxWidth()
        .size(200.dp)
        .background(
          brush = brush
        )
    )
    Spacer(modifier = Modifier.height(8.dp))
    Spacer(
      modifier = Modifier
        .fillMaxWidth()
        .height(30.dp)
        .padding(vertical = 8.dp)
        .background(brush = brush)
    )
    Spacer(
      modifier = Modifier
        .fillMaxWidth()
        .height(30.dp)
        .padding(vertical = 8.dp)
        .background(brush = brush)
    )
  }
}