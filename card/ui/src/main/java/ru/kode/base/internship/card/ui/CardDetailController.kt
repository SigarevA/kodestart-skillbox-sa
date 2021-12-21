package ru.kode.base.internship.card.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.google.accompanist.insets.navigationBarsWithImePadding
import com.google.accompanist.insets.statusBarsPadding
import ru.kode.base.internship.card.ui.CardDetailScreen.ViewIntents
import ru.kode.base.internship.card.ui.CardDetailScreen.ViewState
import ru.kode.base.internship.domain.Card
import ru.kode.base.internship.products.ui.core.uikit.KodeBankBaseController
import ru.kode.base.internship.products.ui.core.uikit.theme.AppTheme
import java.text.SimpleDateFormat
import java.util.Locale

internal class CardDetailController :
  KodeBankBaseController<ViewState, ViewIntents>() {
  override fun createConfig(): Config<ViewIntents> = object : Config<ViewIntents> {
    override val intentsConstructor = ::ViewIntents
  }

  @Composable
  override fun ScreenContent(state: ViewState) {
    Box(
      modifier = Modifier
        .navigationBarsWithImePadding()
    ) {
      Column {
        state.card?.let { DetailCard(it) } ?: Spacer(Modifier.statusBarsPadding())
        Row(
          verticalAlignment = Alignment.CenterVertically,
          modifier = Modifier
            .fillMaxWidth()
            .clickable {
              intents.openDialog()
            }
        ) {
          Icon(
            painter = painterResource(id = R.drawable.ic_pen_edit),
            contentDescription = null,
            modifier = Modifier.padding(
              start = 20.dp,
              top = 17.dp,
              end = 19.dp,
              bottom = 21.dp
            )
          )
          Text(
            text = stringResource(id = R.string.change_name_card),
            style = AppTheme.typography.body2,
            color = AppTheme.colors.textSecondary
          )
        }
      }


      val snackbarHostState = remember { SnackbarHostState() }

      if (state.updateStatus != null) {
        LaunchedEffect(key1 = 2) {
          when (state.updateStatus) {
            CardDetailScreen.UpdateStatus.Success -> snackbarHostState.showSnackbar("0")
            CardDetailScreen.UpdateStatus.Failure -> snackbarHostState.showSnackbar("1")
          }
          intents.dismiss()
        }
      }

      SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { snackBarData ->
          when (snackBarData.message) {
            "1" -> FailureSnackBar(Modifier
              .statusBarsPadding()
              .padding(16.dp)) {
              snackBarData.dismiss()
              intents.dismiss()
            }
            "0" -> SuccessSnackBar(Modifier
              .statusBarsPadding()
              .padding(16.dp)) {
              snackBarData.dismiss()
              intents.dismiss()
            }
          }
        }
      )

    }
    if (state.isOpenDialog) {
      NameChangeDialog(
        state.newName ?: "",
        state.card?.name ?: "",
        dismiss = { intents.closeDialog() },
        change = { newText -> intents.changeCardName(newText) }
      ) {
        intents.clickChangeName()
      }
    }
  }

  @Composable
  fun DetailCard(card: Card) {

    val typeCard: Int = remember(key1 = card) {
      when (card.type) {
        Card.Type.physical -> R.string.physical_card
        Card.Type.digital -> R.string.virtual_card
      }
    }

    val atExpired = remember(key1 = card) {
      val format = SimpleDateFormat("MM/yy", Locale.getDefault())
      format.format(card.expiredAt)
    }

    val numberCard = remember(key1 = card) {
      "**** ${card.number.substring(card.number.length - 4)}"
    }

    Surface(
      color = AppTheme.colors.backgroundSecondary
    ) {
      Column(
        modifier = Modifier.statusBarsPadding()
      ) {
        Spacer(modifier = Modifier.height(8.dp))
        Box(contentAlignment = Alignment.Center) {
          Text(
            text = stringResource(id = R.string.card_title),
            color = AppTheme.colors.textPrimary,
            style = AppTheme.typography.bodyMedium
          )
          Row(modifier = Modifier.fillMaxWidth()) {
            Spacer(modifier = Modifier.width(16.dp))
            IconButton(onClick = { /*TODO*/ }) {
              Icon(
                painterResource(id = R.drawable.ic_back_arrow),
                contentDescription = null
              )
            }
          }
        }

        Box(
          contentAlignment = Alignment.Center,
          modifier = Modifier.fillMaxWidth()
        ) {
          Box(
            modifier = Modifier
              .padding(start = 24.dp, end = 24.dp, top = 27.dp)
              .requiredWidthIn(max = 272.dp)
              .fillMaxWidth()

          ) {
            Image(
              painter = painterResource(id = R.drawable.bg_card),
              contentDescription = null,
              contentScale = ContentScale.Crop,
              modifier = Modifier
                .fillMaxWidth()
                .shadow(
                  elevation = 12.dp,
                  shape = RoundedCornerShape(12.dp)
                )
            )
            Column(
              modifier = Modifier.matchParentSize()
            ) {
              Spacer(modifier = Modifier.height(16.dp))
              Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                  .fillMaxWidth()
              ) {
                Image(
                  painter = painterResource(id = R.drawable.ic_visa),
                  contentDescription = null,
                  contentScale = ContentScale.Crop,
                  modifier = Modifier
                    .padding(16.dp)
                    .height(12.dp)
                )
                Column(modifier = Modifier.weight(1f)) {
                  Text(
                    text = card.name,
                    color = AppTheme.colors.textPrimary,
                    style = AppTheme.typography.body2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                  )
                  Spacer(modifier = Modifier.height(4.dp))
                  if (card.status != Card.Status.DEACTIVATED)
                    Text(
                      text = stringResource(id = typeCard),
                      color = AppTheme.colors.textSecondary,
                      style = AppTheme.typography.caption2,
                      maxLines = 1,
                      overflow = TextOverflow.Ellipsis
                    )
                }
                Icon(
                  painter = painterResource(id = R.drawable.ic_wifi),
                  contentDescription = null,
                  modifier = Modifier
                    .padding(16.dp)
                )
              }
              Spacer(modifier = Modifier.weight(1f))
              Spacer(modifier = Modifier.height(20.dp))
              if (card.status != Card.Status.DEACTIVATED) {
                Text(
                  text = "7 334,00 ₽",
                  color = AppTheme.colors.textPrimary,
                  style = AppTheme.typography.bodySemibold,
                  modifier = Modifier.padding(start = 16.dp)
                )
              } else {
                Text(
                  text = stringResource(id = R.string.card_blocked),
                  color = AppTheme.colors.indicatorContendError,
                  style = AppTheme.typography.bodySemibold,
                  modifier = Modifier.padding(start = 16.dp)
                )
              }
              Spacer(modifier = Modifier.weight(1f))
              Spacer(modifier = Modifier.height(25.dp))
              Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)) {
                Text(
                  text = numberCard,
                  color = AppTheme.colors.textSecondary,
                  style = AppTheme.typography.caption1
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                  text = atExpired,
                  color = AppTheme.colors.textSecondary,
                  style = AppTheme.typography.caption1
                )
              }
              Spacer(modifier = Modifier.height(25.dp))
            }
          }
        }
        Spacer(modifier = Modifier.height(32.dp))
      }
    }
  }

  @Composable
  fun FailureSnackBar(
    modifier: Modifier = Modifier,
    dismiss: () -> Unit,
  ) {
    BasicSnackBar(
      modifier,
      stringResource(id = R.string.changing_name_of_card_failure),
      AppTheme.colors.indicatorContendSuccess,
      dismiss
    )
  }

  @Composable
  fun SuccessSnackBar(
    modifier: Modifier = Modifier,
    dismiss: () -> Unit,
  ) {
    BasicSnackBar(
      modifier,
      stringResource(id = R.string.changing_name_of_card_successfully),
      AppTheme.colors.indicatorContendSuccess,
      dismiss
    )
  }

  @Composable
  fun BasicSnackBar(
    modifier: Modifier = Modifier,
    message: String,
    bgColor: Color,
    dismiss: () -> Unit,
  ) {
    Snackbar(
      modifier = modifier,
      shape = RoundedCornerShape(13.dp),
      backgroundColor = bgColor,
    ) {
      Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
          modifier = Modifier
            .padding(vertical = 16.dp)
            .weight(1f),
          text = message,
          style = AppTheme.typography.body2,
          color = AppTheme.colors.textButton,
        )
        IconButton(
          onClick = { dismiss() }
        ) {
          Icon(
            painter = painterResource(id = R.drawable.ic_close),
            contentDescription = null,
            tint = Color.White
          )
        }
      }
    }
  }

  @Composable
  fun NameChangeDialog(
    newName: String,
    hint: String,
    dismiss: () -> Unit,
    change: (String) -> Unit,
    click: () -> Unit,
  ) {
    Dialog(
      onDismissRequest = { dismiss() }
    ) {
      Box(modifier = Modifier
        .fillMaxWidth(1f)
        .background(color = AppTheme.colors.backgroundPrimary, shape = RoundedCornerShape(14.dp))
      ) {
        Column(
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          Spacer(modifier = Modifier.height(20.dp))
          Text(
            text = stringResource(id = R.string.rename),
            color = AppTheme.colors.textPrimary,
            style = AppTheme.typography.bodySemibold
          )
          Text(
            text = stringResource(id = R.string.input_new_name),
            color = AppTheme.colors.textPrimary,
            style = AppTheme.typography.caption1
          )
          Box(
            modifier = Modifier
              .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 12.dp)
              .fillMaxWidth()
              .border(1.dp, Color(0xA61C1C1E), RoundedCornerShape(5.dp))
              .background(
                color = Color(0xFF1C1C1E),
                shape = RoundedCornerShape(5.dp)
              )
              .padding(all = 5.dp)
          ) {
            BasicTextField(
              value = newName,
              onValueChange = { newText -> change(newText) },
              textStyle = AppTheme.typography.caption1.copy(
                color = AppTheme.colors.textPrimary,
              ),
              maxLines = 1
            )
            if (newName.isEmpty())
              Text(
                hint,
                style = AppTheme.typography.caption1,
                color = Color(0x4DEBEBF5)
              )
          }
          Spacer(modifier = Modifier
            .height(1.dp)
            .fillMaxWidth()
            .background(Color(0xBF1E1E1E))
          )

          Row(
            modifier = Modifier.height(IntrinsicSize.Min)
          ) {
            Text(
              text = stringResource(id = R.string.cancel),
              modifier = Modifier
                .weight(1f)
                .clickable {
                  dismiss()
                }
                .padding(vertical = 11.dp),
              style = AppTheme.typography.bodySemibold,
              color = Color(0xFF0A84FF),
              textAlign = TextAlign.Center
            )
            Divider(
              color = Color(0xBF1E1E1E),
              modifier = Modifier
                .fillMaxHeight()
                .width(1.dp)
            )
            Text(
              text = stringResource(id = R.string.save),
              modifier = Modifier
                .weight(1f)
                .clickable {
                  click()
                }
                .padding(vertical = 11.dp),
              style = AppTheme.typography.bodySemibold,
              color = Color(0xFF0A84FF),
              textAlign = TextAlign.Center
            )
          }
        }
      }
    }
  }
}