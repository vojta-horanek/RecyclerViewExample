package eu.vojtechh.recyclerviewexample

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.SimpleItemAnimator
import dagger.hilt.android.AndroidEntryPoint
import eu.vojtechh.recyclerviewexample.databinding.ActivityMainBinding
import eu.vojtechh.recyclerviewexample.viewmodel.MainViewModel

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        var noteJustAdded = false

        // Set up recycler view
        val noteAdapter = NoteAdapter()
        binding.recycler.adapter = noteAdapter
        (binding.recycler.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

        // Set click listeners for note items
        noteAdapter.setClickListener { _, note -> viewModel.onNoteClick(note) }
        noteAdapter.setLongClickListener { _, note -> viewModel.onNoteLongClick(note) }

        // Observe on notes from repository
        viewModel.notesRepo.observe(this, { notes ->
            viewModel.oneNewRepoNotes(notes)
        })

        // Observe on notes list
        viewModel.notes.observe(this, { notes ->
            noteAdapter.submitList(notes.reversed()) {
                // Only scroll up when a note is added and not when deleted or selected
                if (noteJustAdded) {
                    binding.recycler.smoothScrollToPosition(0)
                    noteJustAdded = false
                }
            }
        })

        viewModel.isDeleteVisible.observe(this, { invalidateOptionsMenu() })

        // Add listener to add button
        binding.buttonAddNote.setOnClickListener {
            viewModel.onAddNote().observe(this, {
                when (it) {
                    NoteAddResponse.SUCCESS -> {
                        binding.editLayoutName.isErrorEnabled = false
                        binding.editLayoutText.isErrorEnabled = false
                        binding.editTextName.text?.clear()
                        binding.editTextText.text?.clear()
                        noteJustAdded = true
                    }
                    NoteAddResponse.ERROR_NAME -> {
                        binding.editLayoutName.isErrorEnabled = true
                        binding.editLayoutName.error = getString(R.string.no_value_given)
                    }
                    NoteAddResponse.ERROR_TEXT -> {
                        binding.editLayoutText.isErrorEnabled = true
                        binding.editLayoutText.error = getString(R.string.no_value_given)
                    }
                    else -> {
                    }
                }
            })
        }

        // Add listeners to text inputs
        binding.editTextName.doOnTextChanged { text, _, _, _ ->
            viewModel.onNameChanged(text.toString())
            binding.editLayoutName.isErrorEnabled = false
        }
        binding.editTextText.doOnTextChanged { text, _, _, _ ->
            viewModel.onTextChanged(text.toString())
            binding.editLayoutText.isErrorEnabled = false
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        viewModel.isDeleteVisible.value?.let {
            menu?.findItem(R.id.delete)?.isVisible = it
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete -> {
                viewModel.onDeletePressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}