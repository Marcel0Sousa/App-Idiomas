package com.marcelo.idiomas

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import com.marcelo.idiomas.datasource.LanguagePreferences
import com.marcelo.idiomas.ui.theme.Blue900
import com.marcelo.idiomas.ui.theme.Gray900
import com.marcelo.idiomas.ui.theme.White
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Home()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home() {

    var expandedMenu by remember { mutableStateOf(false) }
    var language by remember { mutableStateOf("") }
    var flag by remember { mutableStateOf(R.drawable.bandeira1) }
    var flagLanguage by remember { mutableStateOf("Idiomas") }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        LanguagePreferences.getLanguage(context).collect {
            language = it
            updateLocale(context, language)
        }
    }


    Scaffold(topBar = {
        TopAppBar(
            title = { Text(text = stringResource(R.string.app_name), color = White) },
            colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Blue900
        ), actions = {
            Icon(imageVector = Icons.Rounded.Menu,
                contentDescription = "Icon Menu",
                tint = White,
                modifier = Modifier.clickable {
                    expandedMenu = true
                })

            Column(modifier = Modifier) {
                DropdownMenu(
                    expanded = expandedMenu, onDismissRequest = {
                        expandedMenu = false
                    }, modifier = Modifier
                        .width(200.dp)
                        .background(White)
                ) {
                    DropdownMenuItem(text = {
                        Text(text = "Português")
                    }, leadingIcon = {
                        Image(
                            painter = painterResource(R.drawable.brasil),
                            contentDescription = "Brasil",
                            modifier = Modifier.size(30.dp)
                        )
                    }, onClick = {
                        scope.launch {
                            LanguagePreferences.saveLanguage(context = context, languageCode = "pt")
                            expandedMenu = false
                        }
                    })

                    DropdownMenuItem(text = {
                        Text(text = "Inglês")
                    }, leadingIcon = {
                        Image(
                            painter = painterResource(R.drawable.eua),
                            contentDescription = "EUA",
                            modifier = Modifier.size(30.dp)
                        )
                    }, onClick = {
                        scope.launch {
                            LanguagePreferences.saveLanguage(context = context, languageCode = "en")
                            expandedMenu = false
                        }
                    })

                    DropdownMenuItem(text = {
                        Text(text = "Espanhol")
                    }, leadingIcon = {
                        Image(
                            painter = painterResource(R.drawable.espanha),
                            contentDescription = "Espanha",
                            modifier = Modifier.size(30.dp)
                        )
                    }, onClick = {
                        scope.launch {
                            LanguagePreferences.saveLanguage(context = context, languageCode = "es")
                            expandedMenu = false
                        }
                    })
                }
            }
        })
    }) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(White)
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            when(language) {
                "pt" -> {
                    flag = R.drawable.bandeira1
                }
                "en" -> {
                    flag = R.drawable.bandeira2
                }
                "es" -> {
                    flag = R.drawable.bandeira3
                }
            }

            Image(
                painter = painterResource(flag),
                contentDescription = "Brasil",
                modifier = Modifier.size(200.dp)
            )

            Text(
                text = stringResource(R.string.idioma),
                fontSize = 34.sp,
                color = Gray900,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = stringResource(R.string.descricao),
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.CenterHorizontally)
                    .border(width = 2.dp, color = Blue900)
                    .padding(10.dp)
            )
        }
    }
}

private fun updateLocale(context: Context, languageCode: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        context.getSystemService(LocaleManager::class.java)
            .applicationLocales = LocaleList.forLanguageTags(languageCode)
    } else {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageCode))
    }
}

@Preview(showBackground = true)
@Composable
private fun HomePreview() {
    Home()
}