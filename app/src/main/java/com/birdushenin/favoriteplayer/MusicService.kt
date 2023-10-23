import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.birdushenin.favoriteplayer.Constants
import com.birdushenin.favoriteplayer.MainActivity
import com.birdushenin.favoriteplayer.R

class MusicService : Service() {
    private lateinit var mediaPlayer: MediaPlayer
    private val audioFiles = listOf(
        R.raw.song1, R.raw.song2, R.raw.song3
    )
    private var currentTrackIndex = 0
    private var isPlaying = false

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                Constants.NOTIFICATION_CHANNEL_ID,
                "Music Player Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    inner class LocalBinder : Binder(){
        fun getService(): MusicService{
            return this@MusicService
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Constants.ACTION_PLAY -> {
                if (!isPlaying) {
                    playMusic()
                    isPlaying = true
                }
            }
            Constants.ACTION_PAUSE -> {
                if (isPlaying) {
                    pauseMusic()
                    isPlaying = false
                }
            }
            Constants.ACTION_NEXT -> {
                playNext()
            }
            Constants.ACTION_PREVIOUS -> {
                playPrevious()
            }
        }
        return START_STICKY
    }

   fun playMusic() {
        if (!mediaPlayer.isPlaying) {
            val currentAudioFile = audioFiles[currentTrackIndex]
            mediaPlayer = MediaPlayer.create(this, currentAudioFile)
            mediaPlayer.start()
            startForeground(Constants.NOTIFICATION_ID, createNotification())
        }
    }

    private fun pauseMusic() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            stopForeground(false)
        }
    }
    private fun updateNotification() {
        val notification = createNotification()
        startForeground(Constants.NOTIFICATION_ID, notification)
    }

    private fun playNext() {
        if (currentTrackIndex < audioFiles.size - 1) {
            currentTrackIndex++
            playMusic()
            updateNotification()
        }
    }

    private fun playPrevious() {
        if (currentTrackIndex > 0) {
            currentTrackIndex--
            playMusic()
            updateNotification()
        }
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent, 0
        )

        return NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Music Player")
            .setContentText("Song Title")
            .setSmallIcon(R.drawable.ic_music)
            .setContentIntent(pendingIntent)
            .build()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}