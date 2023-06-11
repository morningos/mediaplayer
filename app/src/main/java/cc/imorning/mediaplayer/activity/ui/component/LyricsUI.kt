package cc.imorning.mediaplayer.activity.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun LyricsUI(
    liveTime: Long = 0L,
    lyricsEntryList: String
) {
    // 解析歌词，并按照时间排序
    val entries = parseLyrics(lyricsEntryList)
        .sortedBy { it.time }

    // 在所有歌词文本中，找到与当前时间对应的那句歌词
    val currentEntry = entries.findLast { it.time <= liveTime }

    Column(modifier = Modifier.fillMaxSize()) {
        // Text(text = currentEntry?.text ?: "", textAlign = TextAlign.Center)
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(entries.size) { index ->
                Text(
                    text = entries[index].text,
                    modifier = Modifier.padding(vertical = 4.dp),
                    textAlign = TextAlign.Center,
                    // if (entries[index] == currentEntry) TextAlign.Center else TextAlign.Start,
                    color = if (entries[index].time > liveTime) Color.Gray else Color.Black
                )
            }
        }
    }
}

data class LyricEntry(val time: Long, val text: String)

private fun parseLyrics(rawText: String): List<LyricEntry> {
    return rawText.split("\n")
        .mapNotNull { entryText ->
            val matcher = Regex("\\[(\\d{2}):(\\d{2}).(\\d{2})]\\s*(.*)").find(entryText)
            with(matcher?.groupValues) {
                if (this != null && size >= 5) {
                    val time =
                        this[1].toLong() * 60_000 + this[2].toLong() * 1000 + this[3].toLong()
                    LyricEntry(time, this[4])
                } else {
                    null
                }
            }
        }
}
