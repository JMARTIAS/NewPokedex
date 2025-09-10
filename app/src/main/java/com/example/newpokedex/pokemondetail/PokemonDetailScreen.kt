package com.example.newpokedex.pokemondetail

import android.util.Log // Opcional, para depuración
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import com.example.newpokedex.R
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
// import androidx.compose.ui.focus.focusModifier // No parece usarse
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
// import coil.Coil // No se usa directamente
import coil.compose.AsyncImage
// import coil.util.CoilUtils // No se usa
// import com.android.volley.toolbox.ImageRequest // No se usa, se usa Coil
import java.util.*
import kotlin.math.round
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newpokedex.data.remote.responses.Pokemon
import com.example.newpokedex.data.remote.responses.Type
import com.example.newpokedex.util.Resource
import com.example.newpokedex.util.parseStatToAbbr
import com.example.newpokedex.util.parseStatToColor
import com.example.newpokedex.util.parseTypeToColor

// private const val TAG_DETAIL = "PokemonDetail" // Para logs si los necesitas

@Composable
fun PokemonDetailScreen(
    dominantColor: Color,
    pokemonName: String,
    navController: NavController,
    topPadding: Dp = 20.dp,
    pokemonImageSize: Dp = 200.dp,
    viewModel: PokemonDetailViewModel = hiltViewModel()

) {
    val pokemonInfo = produceState<Resource<Pokemon>>(initialValue = Resource.Loading()) {
        value = viewModel.getPokemonInfo(pokemonName)
    }.value
    Log.d("PokemonDetailScreen", "PokemonInfo State: $pokemonInfo") // <-- AÑADE ESTE LOG

    Box(modifier = Modifier
        .fillMaxSize()
        .background(dominantColor)
        .padding(bottom = 16.dp)
    ) {
        PokemonDetailTopSection(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .align(Alignment.TopCenter)
        )
        PokemonDetailStateWrapper(
            pokemonInfo = pokemonInfo,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = topPadding + pokemonImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .shadow(10.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            loadingModifier = Modifier
                .size(100.dp)
                .align(Alignment.Center)
                .padding(
                    top = topPadding + pokemonImageSize / 2f,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        )

        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            if (pokemonInfo is Resource.Success) {
                pokemonInfo.data?.sprites?.let { sprites ->
                    val imageUrl = sprites.frontDefault
                    if (imageUrl != null) {
                        AsyncImage(
                            model = coil.request.ImageRequest.Builder(LocalContext.current)
                                .data(imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = pokemonInfo.data.name,
                            modifier = Modifier
                                .size(pokemonImageSize)
                                .offset(y = topPadding),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PokemonDetailTopSection(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.Black,
                        Color.Transparent
                    )
                )
            )
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = "Back", // Añadido
            tint = Color.White,
            modifier = Modifier
                .size(36.dp)
                .offset(16.dp, 16.dp)
                .clickable {
                    navController.popBackStack()
                }
        )
    }
}

@Composable
fun PokemonDetailStateWrapper(
    pokemonInfo: Resource<Pokemon>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier
) {
    when(pokemonInfo) {
        is Resource.Success -> {
            PokemonDetailSection(
                pokemonInfo = pokemonInfo.data!!,
                modifier = modifier
                    .offset(y = (-20).dp)
            )
        }
        is Resource.Error -> {
            Text(
                text = pokemonInfo.message ?: "An unknown error occurred", // Manejar mensaje nulo
                color = Color.Red,
                modifier = modifier
            )
        }
        is Resource.Loading -> {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = loadingModifier
            )
        }
    }
}

@Composable
fun PokemonDetailSection(
    pokemonInfo: Pokemon,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .offset(y = 100.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "#${pokemonInfo.id} ${pokemonInfo.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }}",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
        PokemonTypeSection(types = pokemonInfo.types)
        PokemonDetailDataSection(
            pokemonWeight = pokemonInfo.weight,
            pokemonHeight = pokemonInfo.height
        )
        PokemonBaseStats(pokemonInfo = pokemonInfo) // PokemonBaseStats corregido se usará aquí
    }
}

@Composable
fun PokemonTypeSection(types: List<Type>) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(16.dp)
    ) {
        types.forEach { typeEntry -> // Usar forEach
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .clip(CircleShape)
                    .background(parseTypeToColor(typeEntry)) // Asume que typeEntry es el objeto Type
                    .height(35.dp)
            ) {
                Text(
                    text = typeEntry.type.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                    color = Color.White,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
fun PokemonDetailDataSection(
    pokemonWeight: Int,
    pokemonHeight: Int,
    sectionHeight: Dp = 80.dp
) {
    val pokemonWeightInKg = remember(pokemonWeight) { // key para remember
        round(pokemonWeight * 100f) / 1000f
    }
    val pokemonHeightInMeters = remember(pokemonHeight) { // key para remember
        round(pokemonHeight * 100f) / 1000f
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        PokemonDetailDataItem(
            dataValue = pokemonWeightInKg,
            dataUnit = "kg",
            dataIcon = painterResource(id = R.drawable.ic_weight),
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier
            .size(1.dp, sectionHeight)
            .background(Color.LightGray))
        PokemonDetailDataItem(
            dataValue = pokemonHeightInMeters,
            dataUnit = "m",
            dataIcon = painterResource(id = R.drawable.ic_height),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun PokemonDetailDataItem(
    dataValue: Float,
    dataUnit: String,
    dataIcon: Painter,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Icon(painter = dataIcon, contentDescription = dataUnit, tint = MaterialTheme.colorScheme.onSurface) // Añadida descripción
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "$dataValue$dataUnit",
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

// --- VERSIÓN CORREGIDA DE PokemonStat ---
@Composable
fun PokemonStat(
    statName: String,
    statValue: Int,
    statMaxValue: Int,
    statColor: Color,
    height: Dp = 28.dp,
    animDuration: Int = 1000,
    animDelay: Int = 0
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }

    // Calcula el targetPercentage de forma segura para evitar NaN
    val targetPercentage = if (animationPlayed) {
        if (statMaxValue > 0) { // Comprobación crucial
            statValue.toFloat() / statMaxValue.toFloat()
        } else {
            // Si statMaxValue es 0 (o negativo):
            // Si statValue es 0, el porcentaje es 0%.
            // Si statValue > 0 (anómalo si max es 0), el porcentaje es 0% por seguridad.
            0f
        }
    } else {
        0f // Estado inicial antes de que la animación comience
    }

    // Opcional: Log para depurar el targetPercentage si el problema persiste
    // Log.d("PokemonStatDebug", "Stat: $statName, Value: $statValue, Max: $statMaxValue, Target%: $targetPercentage, Played: $animationPlayed")

    val curPercent = animateFloatAsState(
        targetValue = targetPercentage.coerceIn(0f, 1f), // Asegura que el valor esté entre 0 y 1
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = animDelay
        ),
        label = "$statName Percentage Animation" // Etiqueta para depuración
    )

    LaunchedEffect(key1 = true) { // Se activa una vez cuando el Composable entra en la composición
        animationPlayed = true
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(height)
            .clip(CircleShape)
            .background(
                // Considera usar colores del MaterialTheme para consistencia
                if (isSystemInDarkTheme()) Color(0xFF505050) else Color.LightGray
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(curPercent.value) // curPercent.value ahora debería ser seguro
                .clip(CircleShape)
                .background(statColor)
                .padding(horizontal = 8.dp)
        ) {
            Text(
                text = statName,
                fontWeight = FontWeight.Bold
            )
            Text(
                // Muestra el valor original del stat, ya que la barra representa el porcentaje
                text = statValue.toString(),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// --- VERSIÓN CORREGIDA DE PokemonBaseStats ---
@Composable
fun PokemonBaseStats(
    pokemonInfo: Pokemon,
    animDelayPerItem: Int = 100
) {
    // Calcula maxBaseStat de forma robusta
    val maxBaseStat = remember(pokemonInfo.stats) { // Recalcular si los stats cambian
        if (pokemonInfo.stats.isEmpty()) {
            Log.w("PokemonDetailScreen", "PokemonBaseStats: Stats list IS EMPTY for ${pokemonInfo.name}") // <-- LOG

            // Log.w("PokemonBaseStatsDebug", "Stats list is empty for ${pokemonInfo.name}")
            1 // Evitar división por cero si la lista está vacía.
            // Un stat de 0/1 será 0%.
        } else {
            Log.w("PokemonDetailScreen", "PokemonBaseStats: Stats list IS EMPTY for ${pokemonInfo.name}") // <-- LOG

            val max = pokemonInfo.stats.maxOfOrNull { it.baseStat } ?: 0
            // Si el máximo real de los stats es 0, usa 1 como divisor para evitar 0/0.
            // Esto significa que si todos los stats son 0, se mostrarán como 0% de 1.
            if (max == 0) 1 else max
        }
    }
    // Opcional: Log para depurar el maxBaseStat efectivo
    // Log.d("PokemonBaseStatsDebug", "Effective maxBaseStat for ${pokemonInfo.name} is $maxBaseStat")

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Base stats:",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(4.dp))

        if (pokemonInfo.stats.isEmpty()) {
            Text(
                text = "No base stats available for this Pokémon.",
                modifier = Modifier.padding(vertical = 8.dp),
                textAlign = TextAlign.Center
            )
        } else {
            pokemonInfo.stats.forEachIndexed { index, stat -> // Usar forEachIndexed
                // Opcional: Log antes de llamar a PokemonStat individualmente
                // Log.d("PokemonBaseStatsDebug", "Loop: index=$index, statName=${parseStatToAbbr(stat)}, statValue=${stat.baseStat}, maxToPass=$maxBaseStat")
                PokemonStat(
                    statName = parseStatToAbbr(stat),
                    statValue = stat.baseStat,
                    statMaxValue = maxBaseStat, // Se pasa el maxBaseStat calculado y seguro
                    statColor = parseStatToColor(stat),
                    animDelay = index * animDelayPerItem
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}