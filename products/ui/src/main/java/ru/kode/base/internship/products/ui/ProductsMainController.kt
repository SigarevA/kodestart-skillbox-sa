package ru.kode.base.internship.products.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.InfiniteRepeatableSpec
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import ru.kode.base.internship.core.domain.entity.LceState
import ru.kode.base.internship.domain.Card
import ru.kode.base.internship.products.domain.models.Account
import ru.kode.base.internship.products.domain.models.CurrencyType
import ru.kode.base.internship.products.domain.models.Deposit
import ru.kode.base.internship.products.ui.ProductsMainScreen.ViewIntents
import ru.kode.base.internship.products.ui.core.uikit.KodeBankBaseController
import ru.kode.base.internship.products.ui.core.uikit.component.PrimaryButton
import ru.kode.base.internship.products.ui.core.uikit.theme.AppTheme
import ru.kode.base.internship.products.utils.drawable
import ru.kode.base.internship.products.utils.icon
import ru.kode.base.internship.products.utils.stringId
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Locale

internal class ProductsMainController :
  KodeBankBaseController<ProductsMainScreen.ViewState, ViewIntents>() {

  override fun createConfig(): Config<ViewIntents> {
    return object : Config<ViewIntents> {
      override val intentsConstructor = ::ViewIntents
    }
  }

  @Composable
  override fun ScreenContent(state: ProductsMainScreen.ViewState) {
    val transition = rememberInfiniteTransition()
    val animationSpec = infiniteRepeatable<Float>(
      tween(durationMillis = 1200, easing = LinearEasing),
      RepeatMode.Restart
    )
    val stopLists = arrayOf(
      0.1f to AppTheme.colors.contendSecondary.copy(0.5f),
      0.5f to AppTheme.colors.contendTertiary.copy(0.5f),
      0.99f to AppTheme.colors.contendSecondary.copy(0.5f)
    )

    Box(
      contentAlignment = Alignment.TopCenter,
      modifier = Modifier
        .statusBarsPadding()
        .navigationBarsWithImePadding()
    ) {
      SwipeRefresh(
        state = rememberSwipeRefreshState(state.isRefresh),
        onRefresh = { intents.refresh() },
        indicator = { state, trigger ->
          SwipeRefreshIndicator(
            state = state,
            refreshTriggerDistance = trigger,
            scale = true,
            backgroundColor = AppTheme.colors.backgroundSecondary,
            shape = RoundedCornerShape(40.dp),
          )
        }
      ) {
        LazyColumn {
          if (state.depositsLceState == LceState.Loading && state.bankAccountsLceState == LceState.Loading)
            item {
              Spacer(modifier = Modifier.height(50.dp))
              LoadingHeader(
                transition,
                stopLists,
                animationSpec,
                modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
              )
            }
          if (state.depositsLceState == LceState.Content || state.bankAccountsLceState == LceState.Content)
            item {
              ScreenHeader()
            }
          if (state.bankAccountsLceState == LceState.Loading)
            item {
              LoadingBlock(transition, animationSpec, stopLists)
            }
          if (state.bankAccountsLceState == LceState.Content)
            state.bankAccounts?.let { bankinfo ->
              if (bankinfo.bankAccounts.isNotEmpty()) {
                item {
                  HeaderBankAccounts()
                }
                bankinfo.bankAccounts.forEachIndexed { i, bankAccount ->
                  item {
                    BankAccount(bankAccount = bankAccount, i, bankinfo.expandBankAccounts[i])
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
            item { LoadingBlock(transition, animationSpec, stopLists) }
          if (state.depositsLceState == LceState.Content)
            state.deposits?.let { deposits ->
              if (deposits.isNotEmpty()) {
                item {
                  BankDepositHeader()
                }
                deposits.forEachIndexed { i, deposit ->
                  item {
                    BankDeposit(
                      deposit
                    )
                    if (i != deposits.size - 1 && deposits.size != 1)
                      Spacer(modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(AppTheme.colors.backgroundSecondary)
                        .padding(start = (40 + 16 + 16).dp, end = 16.dp)
                        .background(color = AppTheme.colors.contendSecondary)
                      )
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
    val shortNum = remember(key1 = card) {
      card.number.substring(card.number.length - 4)
    }

    Surface(
      color = AppTheme.colors.backgroundSecondary,
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = modifier
          .fillMaxWidth()
          .clickable {
            intents.navigateCard(card)
          },
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          painter = painterResource(id = R.drawable.ic_forward_arrow),
          contentDescription = stringResource(id = R.string.products_detail),
          tint = AppTheme.colors.textTertiary,
          modifier = Modifier.padding(horizontal = 30.dp)
        )

        Column(
          modifier = Modifier
            .padding(end = 16.dp)
            .weight(1f)
        ) {
          Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
              Spacer(modifier = Modifier.height(16.dp))
              Text(
                text = card.name,
                color = AppTheme.colors.contendAccentTertiary,
                style = AppTheme.typography.body2)
              Spacer(modifier = Modifier.height(3.dp))
              if (card.status == Card.Status.ACTIVE)
                Text(
                  text = stringResource(id = card.type.stringId),
                  color = AppTheme.colors.textSecondary,
                  style = AppTheme.typography.caption1)
              else
                Text(stringResource(id = R.string.products_blocked_card),
                  color = AppTheme.colors.indicatorContendError,
                  style = AppTheme.typography.caption1
                )
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
                Text(
                  text = shortNum,
                  color = AppTheme.colors.contendAccentTertiary,
                  style = AppTheme.typography.caption2
                )
                Image(
                  painter = painterResource(id = card.paymentSystem.drawable),
                  contentDescription = card.paymentSystem.name
                )
              }
            }
          }
          Surface(modifier = Modifier.height(1.dp), color = AppTheme.colors.contendSecondary) {}
        }
      }
    }
  }

  @Composable
  fun BankAccount(bankAccount: Account, pos: Int, isOpenDetail: Boolean) {
    val balanceValue = remember(key1 = bankAccount) {
      (DecimalFormat.getInstance(Locale("ru")) as DecimalFormat).let {
        it.applyPattern("#,###,###.00")
        it.format(bankAccount.balance)
      }
    }

    val angle: Float by animateFloatAsState(
      targetValue = if (isOpenDetail) 0F else -180F,
      animationSpec = tween(
        durationMillis = 400,
        easing = FastOutSlowInEasing
      )
    )

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
          Text(
            text = "Счет расчетный",
            color = AppTheme.colors.contendAccentTertiary,
            style = AppTheme.typography.body2
          )
          Spacer(modifier = Modifier.height(2.dp))
          Text(
            text = balance(balanceValue, bankAccount.currency),
            color = AppTheme.colors.contendAccentPrimary,
            style = AppTheme.typography.body2
          )
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
          Icon(
            painter = painterResource(id = R.drawable.ic_expand),
            contentDescription = "Знак монеты",
            modifier = Modifier
              .padding(horizontal = 15.dp, vertical = 11.dp)
              .rotate(angle)

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
  ) {
    val balanceValue = remember(key1 = deposit) {
      (DecimalFormat.getInstance(Locale("ru")) as DecimalFormat).let {
        it.applyPattern("#,###,###.00")
        it.format(deposit.balance)
      }
    }
    val dateValue = remember(key1 = deposit) {
      val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
      format.format(deposit.closeDate)
    }
    val rate = remember(key1 = deposit) {
      String.format(Locale.US, "%.2f", deposit.rate)
    }

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
            Text(deposit.name ?: "",
              color = AppTheme.colors.textPrimary,
              modifier = Modifier.weight(1f),
              style = AppTheme.typography.body2)
            Text(
              text = stringResource(id = R.string.products_bid, rate),
              color = AppTheme.colors.textTertiary,
              style = AppTheme.typography.caption2
            )
          }
          Spacer(modifier = Modifier.height(4.dp))
          Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
              text = balance(
                value = balanceValue,
                currencyType = deposit.currency
              ),
              color = AppTheme.colors.contendAccentPrimary,
              modifier = Modifier.weight(1f),
              style = AppTheme.typography.body2
            )
            Text(
              text = stringResource(id = R.string.products_deposit_closing_date, dateValue),
              color = AppTheme.colors.textTertiary,
              style = AppTheme.typography.caption2)
          }
          Spacer(modifier = Modifier.height(14.dp))
        }
      }
    }
  }

  @Composable
  @ReadOnlyComposable
  fun balance(value: String, currencyType: CurrencyType): String =
    when (currencyType) {
      CurrencyType.RUB -> stringResource(id = R.string.products_currency_ruble, value)
      CurrencyType.DOLLAR -> stringResource(id = R.string.products_currency_dollar, value)
      CurrencyType.EURO -> stringResource(id = R.string.products_currency_euro, value)
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
fun LoadingHeader(
  transition: InfiniteTransition,
  stopColors: Array<Pair<Float, Color>>,
  animateSpec: InfiniteRepeatableSpec<Float>,
  modifier: Modifier = Modifier,
) {
  LoadingTemplate(
    size = Size(160f, 32f),
    offset = ShimmerOffset((-159).dp, 161.dp, (1).dp, 321.dp),
    transition = transition,
    animateSpec = animateSpec,
    stopColors = stopColors,
    modifier = modifier
  )
/*
  val translateAnim4 by with(LocalDensity.current) {
    transition.animateFloat(
      initialValue = -180.dp.toPx(),
      targetValue = 180.dp.toPx(),
      animationSpec = animateSpec
    )
  }

  val translateAnim5 by with(LocalDensity.current) {
    transition.animateFloat(
      initialValue = -20.dp.toPx(),
      targetValue = 340.dp.toPx(),
      animationSpec = animateSpec
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

 */
}


@Composable
fun LoadingTemplate(
  size: Size,
  offset: ShimmerOffset,
  transition: InfiniteTransition,
  animateSpec: InfiniteRepeatableSpec<Float>,
  stopColors: Array<Pair<Float, Color>>,
  modifier: Modifier = Modifier,
) {
  val translateAnimLeft by with(LocalDensity.current) {
    transition.animateFloat(
      initialValue = offset.initLeft.toPx(),
      targetValue = offset.targetLeft.toPx(),
      animationSpec = animateSpec
    )
  }

  val translateAnimRight by with(LocalDensity.current) {
    transition.animateFloat(
      initialValue = offset.initRight.toPx(),
      targetValue = offset.targetRight.toPx(),
      animationSpec = animateSpec
    )
  }

  val brush = Brush.horizontalGradient(
    *stopColors,
    startX = translateAnimLeft,
    endX = translateAnimRight
  )
  Spacer(
    modifier = modifier
      .height(size.height.dp)
      .width(size.width.dp)
      .background(brush, shape = RoundedCornerShape(12.dp))
  )
}

@Composable
fun LoadingBlockHeader(
  transition: InfiniteTransition,
  animateSpec: InfiniteRepeatableSpec<Float>,
  stopColors: Array<Pair<Float, Color>>,
  modifier: Modifier = Modifier,
) {
  LoadingTemplate(
    size = Size(72f, 14f),
    offset = ShimmerOffset((-92).dp, 92.dp, (-20).dp, 164.dp),
    transition = transition,
    animateSpec = animateSpec,
    stopColors = stopColors,
    modifier = modifier
  )
}

@Composable
fun LoadingBlock(
  transition: InfiniteTransition,
  animateSpec: InfiniteRepeatableSpec<Float>,
  stopColors: Array<Pair<Float, Color>>,
  modifier: Modifier = Modifier,
) {
  Column(
    modifier = modifier.background(AppTheme.colors.backgroundSecondary)
  ) {
    Spacer(modifier = Modifier.height(19.dp))
    LoadingBlockHeader(
      transition = transition,
      animateSpec = animateSpec,
      stopColors = stopColors,
      modifier = Modifier.padding(start = 16.dp)
    )
    Spacer(modifier = Modifier.height(19.dp))
    repeat(3) {
      LoadingItem(
        transition,
        stopColors,
        animationSpec = animateSpec,
        isLast = it == 2
      )
    }
  }
}

@Immutable
data class ShimmerOffset(
  val initLeft: Dp,
  val targetLeft: Dp,
  val initRight: Dp,
  val targetRight: Dp,
)