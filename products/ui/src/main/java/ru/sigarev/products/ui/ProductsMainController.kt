package ru.sigarev.products.ui

import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import ru.kode.base.internship.core.domain.entity.LceState
import ru.kode.base.internship.ui.core.uikit.KodeBankBaseController
import ru.kode.base.internship.ui.core.uikit.component.PrimaryButton
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
      LazyColumn {
        if (state.depositsLceState == LceState.Loading && state.bankAccountsLceState == LceState.Loading)
          item {
            LoadingHeader(
              arrayOf(
                0.0f to AppTheme.colors.contendTertiary.copy(0.5f),
                0.1f to Color(0xFF403A47).copy(0.5f),
                0.5f to AppTheme.colors.contendTertiary.copy(0.5f),
                0.99f to Color(0xFF403A47).copy(0.5f),
                1f to AppTheme.colors.contendTertiary.copy(0.5f),
              )
            )
          }
        if (state.depositsLceState == LceState.Content || state.bankAccountsLceState == LceState.Content)
          item {
            ScreenHeader()
          }
        if (state.bankAccountsLceState == LceState.Loading)
          item {

            LoadingBlock()
          }
        if (state.bankAccountsLceState == LceState.Content)
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
          item { LoadingBlock() }
        if (state.depositsLceState == LceState.Content)
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
        if (state.depositsLceState == LceState.Content && state.bankAccountsLceState == LceState.Content)
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
        if (state.isPartLoadingError)
          item {
            ErrorLoadingPartOfContent()
          }
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

@Composable
fun LoadingItem(
  transition: InfiniteTransition,
  stopColors: Array<Pair<Float, Color>>,
  animationSpec: InfiniteRepeatableSpec<Float>,
  isLast: Boolean = false,
) {

  val translateAnim2 by transition.animateFloat(
    initialValue = with(LocalDensity.current) {
      -140.dp.toPx()
    },
    targetValue = with(LocalDensity.current) {
      140.dp.toPx()
    },
    animationSpec = animationSpec
  )
  val translateAnim3 by transition.animateFloat(
    initialValue = with(LocalDensity.current) {
      -20.dp.toPx()
    },
    targetValue = with(LocalDensity.current) {
      260.dp.toPx()
    },
    animationSpec = animationSpec
  )


  val brush = Brush.horizontalGradient(
    *stopColors,
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

      val translateAnim4 by with(LocalDensity.current) {
        transition.animateFloat(
          initialValue = (-maxWidth - maxWidth.div(2)).toPx(),
          targetValue = (maxWidth + maxWidth.div(2)).toPx(),
          animationSpec = animationSpec
        )
      }

      val translateAnim5 by with(LocalDensity.current) {
        transition.animateFloat(
          initialValue = -maxWidth.div(2).toPx(),
          targetValue = (maxWidth + maxWidth + maxWidth.div(2)).toPx(),
          animationSpec = animationSpec
        )
      }

      val br = Brush.horizontalGradient(
        *stopColors,
        startX = translateAnim4,
        endX = translateAnim5
      )

      Column {
        Spacer(modifier = Modifier.height(16.dp))
        Spacer(modifier = Modifier
          .height(16.dp)
          .fillMaxSize()
          .background(br, shape = RoundedCornerShape(12.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Spacer(modifier = Modifier
          .height(12.dp)
          .fillMaxSize(0.55f)
          .background(br, shape = RoundedCornerShape(12.dp))
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (!isLast)
          Spacer(modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(AppTheme.colors.contendSecondary)
          )
      }
    }
    Spacer(modifier = Modifier
      .width(16.dp)
    )
  }
}

@Composable
fun ErrorLoadingPartOfContent() {
  Column(
    modifier = Modifier.fillMaxWidth(),
    horizontalAlignment = Alignment.CenterHorizontally) {
    Spacer(modifier = Modifier.height(35.dp))
    Text(
      stringResource(id = R.string.products_error_part_loading_title),
      color = AppTheme.colors.textPrimary,
      style = AppTheme.typography.bodyMedium)
    Spacer(modifier = Modifier.height(4.dp))
    Text(stringResource(id = R.string.products_error_part_loading_msg),
      color = AppTheme.colors.textSecondary,
      style = AppTheme.typography.body2,
      textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(16.dp))
    TextButton(onClick = { /*TODO*/ }) {
      Text(
        stringResource(id = R.string.products_error_part_loading_action),
        color = AppTheme.colors.primaryButton,
        style = AppTheme.typography.bodySemibold
      )
    }
    Spacer(modifier = Modifier.height(35.dp))
  }
}

internal val ProductsMainScreen.ViewState.isPartLoadingError: Boolean
  get() =
    (this.bankAccountsLceState is LceState.Error && this.depositsLceState !is LceState.Error) ||
      (this.depositsLceState is LceState.Error && bankAccountsLceState !is LceState.Error)


@Composable
fun LoadingHeader(stopColors: Array<Pair<Float, Color>>) {
  val transition = rememberInfiniteTransition()

  val translateAnim4 by with(LocalDensity.current) {
    transition.animateFloat(
      initialValue = -180.dp.toPx(),
      targetValue = 180.dp.toPx(),
      animationSpec = infiniteRepeatable(
        tween(durationMillis = 1200, easing = LinearEasing),
        RepeatMode.Restart
      )
    )
  }

  val translateAnim5 by with(LocalDensity.current) {
    transition.animateFloat(
      initialValue = -20.dp.toPx(),
      targetValue = 340.dp.toPx(),
      animationSpec = infiniteRepeatable(
        tween(durationMillis = 1200, easing = LinearEasing),
        RepeatMode.Restart
      )
    )
  }

  val brush = Brush.horizontalGradient(
    *stopColors,
    startX = translateAnim4,
    endX = translateAnim5
  )

  Spacer(
    modifier = Modifier
      .padding(start = 16.dp)
      .height(32.dp)
      .width(160.dp)
      .background(brush, shape = RoundedCornerShape(12.dp))
  )
}

@Composable
fun LoadingBlock() {
  val transition = rememberInfiniteTransition()
  val col = arrayOf(
    0.0f to AppTheme.colors.contendTertiary.copy(0.5f),
    0.1f to Color(0xFF403A47).copy(0.5f),
    0.5f to AppTheme.colors.contendTertiary.copy(0.5f),
    0.99f to Color(0xFF403A47).copy(0.5f),
    1f to AppTheme.colors.contendTertiary.copy(0.5f),
  )
  Column(
    modifier = Modifier.background(AppTheme.colors.backgroundSecondary)
  ) {
    repeat(3) {
      LoadingItem(
        transition,
        col,
        infiniteRepeatable(
          tween(durationMillis = 1200, easing = LinearEasing),
          RepeatMode.Restart
        ),
        it == 2
      )
    }
  }
}