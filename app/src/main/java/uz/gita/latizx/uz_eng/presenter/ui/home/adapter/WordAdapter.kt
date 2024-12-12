package uz.gita.latizx.uz_eng.presenter.ui.home.adapter

import android.annotation.SuppressLint
import android.database.Cursor
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import uz.gita.latizx.uz_eng.R
import uz.gita.latizx.uz_eng.data.model.DictionaryModel
import uz.gita.latizx.uz_eng.databinding.ItemWordBinding
import uz.gita.latizx.uz_eng.presenter.ui.home.adapter.WordAdapter.WordViewHolder
import uz.gita.latizx.uz_eng.util.animateScaleAndRotate

class WordAdapter : RecyclerView.Adapter<WordViewHolder>() {
    private var cursor: Cursor? = null
    private var lastOpened = -1
    private var isEng = true
    private var onShareClickListener: ((DictionaryModel?) -> Unit)? = null
    private var onSaveClickListener: ((DictionaryModel?) -> Unit)? = null
    private var onCopyClickListener: ((DictionaryModel?) -> Unit)? = null
    private var onTTSClickListener: ((DictionaryModel?) -> Unit)? = null
    private var onSlowlyTTSClickListener: ((DictionaryModel?) -> Unit)? = null

    inner class WordViewHolder(private val binding: ItemWordBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                btnCopy.setOnClickListener { onCopyClickListener?.invoke(getItem(adapterPosition)) }
                btnFast.setOnClickListener {
                    onTTSClickListener?.invoke(getItem(adapterPosition))
                    btnFast.animateScaleAndRotate()
                }
                btnShare.setOnClickListener { onShareClickListener?.invoke(getItem(adapterPosition)) }
                btnSlowly.setOnClickListener {
                    onSlowlyTTSClickListener?.invoke(getItem(adapterPosition))
                    btnSlowly.animateScaleAndRotate()
                }
                btnSave.setOnClickListener {
                    binding.btnSave.isSelected = getItem(adapterPosition)?.isFavourite == 1
                    onSaveClickListener?.invoke(getItem(adapterPosition))
                }
            }
            binding.root.setOnClickListener {
                if (lastOpened != adapterPosition) {
                    if (lastOpened != -1) notifyItemChanged(lastOpened, Unit)
                    lastOpened = adapterPosition
                } else lastOpened = -1
                changeVisibility(lastOpened == adapterPosition)
                notifyItemChanged(adapterPosition, Unit)
            }
        }

        fun bind(dictionaryModel: DictionaryModel) {
            binding.apply {
                tvWord.text = if (isEng) dictionaryModel.english else dictionaryModel.uzbek
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

                tvType.visibility = if (isVisible) View.VISIBLE else View.GONE
                tvTranscript.visibility = if (isVisible) View.VISIBLE else View.GONE
                tvTrans.visibility = if (isVisible) View.VISIBLE else View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder =
        WordViewHolder(ItemWordBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        cursor?.let {
            it.moveToPosition(position)
            val id = it.getInt(it.getColumnIndexOrThrow("id"))
            val english = it.getString(it.getColumnIndexOrThrow("english"))
            val uzbek = it.getString(it.getColumnIndexOrThrow("uzbek"))
            val type = it.getString(it.getColumnIndexOrThrow("type"))
            val transcript = it.getString(it.getColumnIndexOrThrow("transcript"))
            val countable = it.getString(it.getColumnIndexOrThrow("countable"))
            val isFavourite = it.getInt(it.getColumnIndexOrThrow("is_favourite"))
            holder.bind(
                DictionaryModel(
                    id = id, english = english, uzbek = uzbek, type = type, transcript = transcript, countable = countable,
                    isFavourite = isFavourite, from = "", date = System.currentTimeMillis(),
                )
            )
        }
    }

    override fun getItemCount(): Int = cursor?.count ?: 0

    private fun getItem(position: Int): DictionaryModel? {
        cursor?.let {
            if (it.moveToPosition(position)) {
                val id = it.getInt(it.getColumnIndexOrThrow("id"))
                val english = it.getString(it.getColumnIndexOrThrow("english"))
                val uzbek = it.getString(it.getColumnIndexOrThrow("uzbek"))
                val type = it.getString(it.getColumnIndexOrThrow("type"))
                val transcript = it.getString(it.getColumnIndexOrThrow("transcript"))
                val countable = it.getString(it.getColumnIndexOrThrow("countable"))
                val isFavourite = it.getInt(it.getColumnIndexOrThrow("is_favourite"))
                return DictionaryModel(
                    id = id, english = english, uzbek = uzbek, type = type, transcript = transcript, countable = countable,
                    isFavourite = isFavourite, from = "", date = System.currentTimeMillis(),
                )
            }
        }
        return null
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitCursor(cursor: Cursor?) {
        Log.d("TTT", "submitCursor: ${cursor?.count}")
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

    fun setSaveClickLikeListener(like: (DictionaryModel?) -> Unit) {
        onSaveClickListener = like
    }

    fun setCopyClickListener(listener: (DictionaryModel?) -> Unit) {
        onCopyClickListener = listener
    }

    fun setTTSClickLikeListener(like: (DictionaryModel?) -> Unit) {
        onTTSClickListener = like
    }

    fun setSlowlyClickListener(listener: (DictionaryModel?) -> Unit) {
        onSlowlyTTSClickListener = listener
    }
}