package uz.gita.latizx.uz_eng.presenter.ui.home.adapter

import android.annotation.SuppressLint
import android.database.Cursor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.Balloon
import uz.gita.latizx.uz_eng.R
import uz.gita.latizx.uz_eng.data.mapper.Mapper.toDictionaryModel
import uz.gita.latizx.uz_eng.data.model.DictionaryModel
import uz.gita.latizx.uz_eng.databinding.ItemWordBinding
import uz.gita.latizx.uz_eng.presenter.ui.home.adapter.WordAdapter.WordViewHolder
import uz.gita.latizx.uz_eng.util.TextToSpeechHealer
import uz.gita.latizx.uz_eng.util.highlightSearch
import uz.gita.latizx.uz_eng.util.startScaleAnim
import uz.gita.latizx.uz_eng.util.stopScaleAnim
import javax.inject.Inject

class WordAdapter : RecyclerView.Adapter<WordViewHolder>() {
    private var cursor: Cursor? = null
    private var lastOpened = -1
    private var isEng = true
    private var searchQuery = ""
    private var onShareClickListener: ((DictionaryModel?) -> Unit)? = null
    private var onDetailClickListener: ((DictionaryModel?) -> Unit)? = null
    private var onSaveClickListener: ((DictionaryModel?, position: Int) -> Unit)? = null
    private var onCopyClickListener: ((DictionaryModel?) -> Unit)? = null
    private lateinit var balloon: Balloon

    @Inject
    lateinit var ttsHelper: TextToSpeechHealer

    inner class WordViewHolder(private val binding: ItemWordBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            ttsHelper = TextToSpeechHealer(binding.root.context)
            binding.apply {
                btnCopy.setOnClickListener {
                    val data = getItem(adapterPosition)
                    onCopyClickListener?.invoke(data)
                    balloon = Balloon.Builder(binding.root.context)
                        .setText("${data?.english} copied")
                        .setTextSize(12f)
                        .setTextColorResource(R.color.black)
                        .setBackgroundColorResource(R.color.toast)
                        .setArrowSize(10)
                        .setArrowPosition(0.5f)
                        .setArrowOrientation(ArrowOrientation.TOP)
                        .setCornerRadius(8f)
                        .setPadding(8)
                        .setAutoDismissDuration(1500L)
                        .setMargin(4)
                        .build()

                    balloon.showAlignTop(binding.btnCopy)
//                    it.postDelayed({ balloon.dismiss() }, 1200)
                }
                btnFast.setOnClickListener {
                    btnFast.startScaleAnim()
                    ttsHelper.setPitch(1f)
                    ttsHelper.setSpeechRate(1f)
                    ttsHelper.speak(
                        text = getItem(adapterPosition)?.english.toString(),
                        onStop = { btnFast.stopScaleAnim() }
                    )
                }
                btnShare.setOnClickListener { onShareClickListener?.invoke(getItem(adapterPosition)) }
                btnDocument.setOnClickListener { onDetailClickListener?.invoke(getItem(adapterPosition)) }

                btnSave.setOnClickListener {
                    binding.btnSave.isSelected = getItem(adapterPosition)?.isFavourite == 1
                    getItem(adapterPosition)?.isFavourite = getItem(adapterPosition)?.isFavourite?.xor(1)
                    onSaveClickListener?.invoke(getItem(adapterPosition), adapterPosition)
                }
            }
            binding.root.setOnClickListener {
                if (lastOpened != adapterPosition) {
                    if (lastOpened != -1) notifyItemChanged(lastOpened, Unit)
                    lastOpened = adapterPosition
                } else lastOpened = -1
                Log.d("TTT", ": ${getItem(adapterPosition)?.id}")
                changeVisibility(lastOpened == adapterPosition)
                notifyItemChanged(adapterPosition, Unit)
            }
        }

        fun bind(dictionaryModel: DictionaryModel) {
            binding.apply {
                val fullText = if (isEng) dictionaryModel.english else dictionaryModel.uzbek

                if (searchQuery.isEmpty()) tvWord.text = fullText
                else tvWord.highlightSearch(fullText ?: "", searchQuery)

                tvTrans.text = if (isEng) dictionaryModel.uzbek else dictionaryModel.english
                tvType.text = dictionaryModel.type
                tvTranscript.text = dictionaryModel.transcript
                btnSave.isSelected = dictionaryModel.isFavourite == 1

                if (adapterPosition == lastOpened) changeVisibility(true)
                else changeVisibility(false)
            }
        }

        private fun changeVisibility(isVisible: Boolean) {
            val upAnim = AnimationUtils.loadAnimation(binding.tvTrans.context, R.anim.up)
            val downAnim = AnimationUtils.loadAnimation(binding.tvTrans.context, R.anim.down)
            binding.apply {
                if (isVisible) root.startAnimation(downAnim)
                else root.startAnimation(upAnim)
                llTitle.isSelected = isVisible
                clItemWord.isSelected = isVisible
                tvType.visibility = if (isVisible) View.VISIBLE else View.GONE
                tvTranscript.visibility = if (isVisible) View.VISIBLE else View.GONE
                tvTrans.visibility = if (isVisible) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder =
        WordViewHolder(ItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        cursor?.let { cursor ->
            cursor.moveToPosition(position)
            holder.bind(cursor.toDictionaryModel())
        }
    }

    override fun getItemCount(): Int = cursor?.count ?: 0

    private fun getItem(position: Int): DictionaryModel? {
        cursor?.let { cursor ->
            if (cursor.moveToPosition(position))
                return cursor.toDictionaryModel()
        }
        return null
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitCursor(cursor: Cursor?) {
        this.cursor = cursor
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateLang(isEng: Boolean) {
        this.isEng = isEng
        notifyDataSetChanged()
    }

    fun setShareClickListener(listener: (DictionaryModel?) -> Unit) {
        onShareClickListener = listener
    }

    fun setSaveClickLikeListener(like: (DictionaryModel?, position: Int) -> Unit) {
        onSaveClickListener = like
    }

    fun setCopyClickListener(listener: (DictionaryModel?) -> Unit) {
        onCopyClickListener = listener
    }

    fun setDetailClickListener(listener: (DictionaryModel?) -> Unit) {
        onDetailClickListener = listener
    }

    fun searchQuery(text: String) {
        searchQuery = text
    }
}