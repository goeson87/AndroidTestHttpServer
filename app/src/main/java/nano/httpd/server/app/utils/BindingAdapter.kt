package nano.httpd.server.app.utils

import android.widget.TextView
import androidx.databinding.BindingAdapter


@BindingAdapter("textAppend")
fun bindAppendText(textView: TextView, textAppend: String?){
    textAppend?.let {
        textView.append(textAppend)
        textView.append("\n")
    }

}