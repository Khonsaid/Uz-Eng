package uz.gita.latizx.uz_eng.presenter.ui.detail.adapter

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import uz.gita.latizx.uz_eng.R
import uz.gita.latizx.uz_eng.data.model.WordDetailModel
import uz.gita.latizx.uz_eng.databinding.ItemDetailBinding

class DetailAdapter(private val wordList: List<WordDetailModel>) : RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {

    inner class DetailViewHolder(private val binding: ItemDetailBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(wordDetail: WordDetailModel) {
            binding.apply {
                wordDetail.meanings?.let { list ->
                    // Barcha meaningsni birlashtirish uchun StringBuilder ishlatish
                    list.forEachIndexed { _, meaning ->
//                        definitionsBuilder.append("${index + 1}. ${meaning.partOfSpeech}\n")
                        tvPartOfSpeech.apply {
                            text = meaning.partOfSpeech
                            setTextColor(ContextCompat.getColor(root.context, getColor(meaning.partOfSpeech)))
                        }

                        // Definitions
                        meaning.definitions?.forEachIndexed { defIndex, definition ->
                            tvDefinitions.append("  ${defIndex + 1}) ${definition.definition}\n")
                            definition.example?.let { example ->
                                val spannableExample = SpannableStringBuilder("    Example: $example\n").apply {
                                    setSpan(
                                        ForegroundColorSpan(Color.BLUE),
                                        0,
                                        "    Example:".length,
                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                    )
                                }
                                tvDefinitions.append(spannableExample)
                            }
                        }
                    }

                    // Synonymsni ko'rsatish
                    val synonyms = list.flatMap { it.synonyms ?: emptyList() }
                    if (synonyms.isNotEmpty()) {
                        tvSynonymsTitle.visibility = View.VISIBLE
                        tvSynonyms.visibility = View.VISIBLE
                        tvSynonyms.text = synonyms.joinToString(", ")
                    } else {
                        tvSynonymsTitle.visibility = View.GONE
                        tvSynonyms.visibility = View.GONE
                    }

                    // Antonymsni ko'rsatish
                    val antonyms = list.flatMap { it.antonyms ?: emptyList() }
                    if (antonyms.isNotEmpty()) {
                        tvAntonymsTitle.visibility = View.VISIBLE
                        tvAntonyms.visibility = View.VISIBLE
                        tvAntonyms.text = antonyms.joinToString(", ")
                    } else {
                        tvAntonymsTitle.visibility = View.GONE
                        tvAntonyms.visibility = View.GONE
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder =
        DetailViewHolder(ItemDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.bind(wordList[position])
    }

    override fun getItemCount() = wordList.size

    private fun getColor(partOfSpeech: String?): Int = when (partOfSpeech) {
        "noun" -> R.color.part_of_speech1
        "pronoun" -> R.color.part_of_speech2
        "verb" -> R.color.part_of_speech3
        "adjective" -> R.color.part_of_speech4
        "adverb" -> R.color.part_of_speech5
        "preposition" -> R.color.part_of_speech6
        "conjunction" -> R.color.part_of_speech7
        "interjection" -> R.color.ll_word
        else -> R.color.black
    }
}