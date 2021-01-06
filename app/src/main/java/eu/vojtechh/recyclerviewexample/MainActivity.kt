package eu.vojtechh.recyclerviewexample

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MainActivity : AppCompatActivity(), NoteAdapter.NoteItemListener {

    private lateinit var viewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        setContentView(R.layout.activity_main)

        // Find views
        val recyclerView = findViewById<RecyclerView>(R.id.recycler)
        val editTextName = findViewById<TextInputEditText>(R.id.edit_text_name)
        val editLayoutName = findViewById<TextInputLayout>(R.id.edit_layout_name)
        val editTextText = findViewById<TextInputEditText>(R.id.edit_text_text)
        val editLayoutText = findViewById<TextInputLayout>(R.id.edit_layout_text)
        val buttonAddNote = findViewById<MaterialButton>(R.id.button_add)

        // Set up recycler view
        val noteAdapter = NoteAdapter(this)
        recyclerView.adapter = noteAdapter

        // Observe on notes list
        viewModel.notes.observe(this, { notes ->
            noteAdapter.submitList(notes.reversed())
        })

        viewModel.isDeleteVisible.observe(this, { invalidateOptionsMenu() })

        // Add listener to add button
        buttonAddNote.setOnClickListener {
            when (viewModel.onAddNote()) {
                NoteAddResponse.SUCCESS -> {
                    editLayoutName.isErrorEnabled = false
                    editLayoutText.isErrorEnabled = false
                    editTextName.text?.clear()
                    editTextText.text?.clear()
                    recyclerView.scrollToPosition(0)
                }
                NoteAddResponse.ERROR_NAME -> {
                    editLayoutName.isErrorEnabled = true
                    editLayoutName.error = getString(R.string.no_value_given)
                }
                NoteAddResponse.ERROR_TEXT -> {
                    editLayoutText.isErrorEnabled = true
                    editLayoutText.error = getString(R.string.no_value_given)
                }
            }
        }

        // Add listeners to text inputs
        editTextName.doOnTextChanged { text, _, _, _ ->
            viewModel.onNameChanged(text.toString())
            editLayoutName.isErrorEnabled = false
        }
        editTextText.doOnTextChanged { text, _, _, _ ->
            viewModel.onTextChanged(text.toString())
            editLayoutText.isErrorEnabled = false
        }
    }

    override fun onNoteClick(view: View, note: NoteModel) = viewModel.onNoteClick()

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