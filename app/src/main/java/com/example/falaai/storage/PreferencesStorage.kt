import android.content.Context
import android.content.SharedPreferences

open class PreferencesStorage(private val context: Context) {
    private val keyPreferences = "preferences"

    fun getPreferences(): SharedPreferences = context.getSharedPreferences(keyPreferences, 0)

    fun getEditor(): SharedPreferences.Editor = getPreferences().edit()
}