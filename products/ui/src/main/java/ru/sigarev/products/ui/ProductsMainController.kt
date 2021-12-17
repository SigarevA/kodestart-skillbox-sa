package ru.sigarev.products.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
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
        CircularProgressIndicator(
          modifier = Modifier
            .padding(end = 16.dp),
          color = AppTheme.colors.contendAccentPrimary,
          strokeWidth = 1.5.dp
        )
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