package com.app.composepagination.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.app.composepagination.presentation.intent.ItemIntent
import com.app.composepagination.presentation.viewModel.ItemViewModel

@Composable
fun ItemScreen(viewModel: ItemViewModel = hiltViewModel()) {
    val state by viewModel.state.collectAsState()
    val lazyItems = viewModel.pagingItems.collectAsLazyPagingItems()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.sendIntent(ItemIntent.LoadItems)
    }

    LazyColumn {
        items(lazyItems.itemCount) { index ->
            val item = lazyItems[index]
            item?.let {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = it.name, style = MaterialTheme.typography.bodyLarge)

                    it.imageUrl?.let { imageUrl ->
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    it.videoUrl?.let { videoUrl ->
                        val exoPlayer = remember(context) {
                            ExoPlayer.Builder(context).build().apply {
                                setMediaItem(MediaItem.fromUri(videoUrl))
                                prepare()
                                playWhenReady = false
                            }
                        }

                        AndroidView(
                            factory = {
                                PlayerView(it).apply {
                                    player = exoPlayer
                                    useController = true
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(240.dp)
                        )
                    }
                }
            }
        }

        item {
            when {
                lazyItems.loadState.refresh is LoadState.Loading -> {
                    CircularProgressIndicator(Modifier.padding(16.dp))
                }
                lazyItems.loadState.append is LoadState.Loading -> {
                    CircularProgressIndicator(Modifier.padding(16.dp))
                }
                lazyItems.loadState.refresh is LoadState.Error -> {
                    val error = lazyItems.loadState.refresh as LoadState.Error
                    Text("Error: ${error.error.message}")
                }
            }
        }
    }
}