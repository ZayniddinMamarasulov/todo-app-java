package uz.mamarasulov.todoappjava.ui

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import uz.mamarasulov.todoappjava.AlarmReceiver
import uz.mamarasulov.todoappjava.R
import uz.mamarasulov.todoappjava.model.Note
import uz.mamarasulov.todoappjava.util.AppConstants
import uz.mamarasulov.todoappjava.util.AppUtils
import java.text.SimpleDateFormat
import java.util.*


class AddTodoActivity : AppCompatActivity(), View.OnClickListener {


    lateinit var myCalendar: Calendar

    lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    lateinit var timeSetListener: TimePickerDialog.OnTimeSetListener


    lateinit var editTitle: EditText
    lateinit var editDesc: EditText
    private val textTime: TextView? = null
    lateinit var btnDone: TextView
    lateinit var toolbarTitle: TextView
    lateinit var btnDelete: ImageView
    lateinit var editSetTime: EditText
    lateinit var editSetDate: EditText

    var note: Note? = null

    private var finalDate = ""
    private var finalTime = ""
    private var finalTitle = ""
    private var finalTask = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_todo)
        AppUtils.makeStatusBarDark(this)

        findViewById<View>(R.id.edtTitle).requestFocus()

        toolbarTitle = findViewById<TextView>(R.id.title)
        editTitle = findViewById(R.id.edtTitle)
        editDesc = findViewById<EditText>(R.id.edtTask)
        editSetTime = findViewById<EditText>(R.id.edtSetTime)
        editSetDate = findViewById<EditText>(R.id.edtSetDate)

        btnDelete = findViewById(R.id.btn_close)
        btnDelete.setOnClickListener(this)

        btnDone = findViewById<TextView>(R.id.btn_done)
        btnDone.setOnClickListener(this)
        editSetTime.setOnClickListener(this)
        editSetDate.setOnClickListener(this)

        myCalendar = Calendar.getInstance()

        note = (intent.getSerializableExtra(AppConstants.INTENT_TASK) as Note?)
        if (note == null) {
            toolbarTitle.setText(getString(R.string.add_task_title))
            btnDelete.setImageResource(R.drawable.btn_done)
            btnDelete.setTag(R.drawable.btn_done)
            textTime?.text = AppUtils.getFormattedDateString(AppUtils.getCurrentDateTime())
        } else {
            toolbarTitle.setText(getString(R.string.edit_task_title))
            btnDelete.setImageResource(R.drawable.ic_delete)
            btnDelete.setTag(R.drawable.ic_delete)
            if (note?.getTitle() != null && !note?.getTitle()?.isEmpty()!!) {
                editTitle.setText(note?.getTitle())
                editTitle.setSelection(editTitle.getText().length)
            }
            if (note?.getDescription() != null && !note?.getDescription()?.isEmpty()!!) {
                editDesc.setText(note?.getDescription())
                editDesc.setSelection(editDesc.getText().length)
            }
            if (note?.getCreatedAt() != null) {
                textTime?.text = AppUtils.getFormattedDateString(note?.getCreatedAt())
            }
        }

        AppUtils.openKeyboard(applicationContext)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.edtSetDate -> {
                dateAndTime()
                setDate()
            }
            R.id.edtSetTime -> {
                dateAndTime()
                setTime()
            }
            R.id.imgCancelDate -> {
                editSetDate?.setText("")
                finalDate = ""
//                imgCancelDate.visibility = View.GONE
//                if (relativeLayoutTime.visibility == View.VISIBLE) {
//                    relativeLayoutTime.visibility = View.GONE
//                    edtSetTime.setText("")
//                    finalTime = ""
//                    imgCancelTime.visibility = View.GONE
//                }

            }
            R.id.imgCancelTime -> {
                editSetTime?.setText("")
                finalTime = ""
//                imgCancelTime.visibility = View.GONE
            }
            R.id.btn_close -> {
                if (btnDelete.tag as Int === R.drawable.btn_done) {
                    setResult(Activity.RESULT_CANCELED)
                } else {
                    val intent = intent
                    intent.putExtra(AppConstants.INTENT_DELETE, true)
                    intent.putExtra(AppConstants.INTENT_TASK, note)
                    setResult(Activity.RESULT_OK, intent)
                }
                finish()
                overridePendingTransition(R.anim.stay, R.anim.slide_down)
            }
            R.id.btn_done -> {
                val intent = intent
                if (note != null) {
                    note!!.title = editTitle!!.text.toString()
                    note!!.description = editDesc!!.text.toString()
                    intent.putExtra(AppConstants.INTENT_TASK, note)
                } else {
                    if (editSetTime.text.length > 0)
                        setNotification(myCalendar)
                    intent.putExtra(AppConstants.INTENT_TITLE, editTitle!!.text.toString())
                    intent.putExtra(AppConstants.INTENT_DESC, editDesc!!.text.toString())
                }
                setResult(Activity.RESULT_OK, intent)
                finish()
                overridePendingTransition(R.anim.stay, R.anim.slide_down)
            }
        }
        try {
            AppUtils.hideKeyboard(this)
        } catch (e: Exception) {
            Log.d("exception", e.message!!)
        }
    }

    private fun setNotification(myCalendar: Calendar) {

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(this, AlarmReceiver::class.java)
//        intent.putExtra(Constant.TASK_ID, taskRowId)

        intent.action = "android.intent.action.NOTIFY"
        intent.putExtra(AppConstants.INTENT_TITLE, note?.title)
        intent.putExtra(AppConstants.INTENT_TASK, note?.description)

        val pendingIntent = PendingIntent.getBroadcast(this, 0/*taskRowId*/, intent, PendingIntent.FLAG_ONE_SHOT)
        alarmManager.set(AlarmManager.RTC_WAKEUP, myCalendar.timeInMillis, pendingIntent)

    }

    private fun dateAndTime() {

        myCalendar = Calendar.getInstance()

        dateSetListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabelDate()
        }

        timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            myCalendar.set(Calendar.MINUTE, minute)
            updateLabelTime()
        }

    }

    private fun setTime() {
        val timePickerDialog = TimePickerDialog(this, timeSetListener, myCalendar.get(Calendar.HOUR_OF_DAY),
                myCalendar.get(Calendar.MINUTE), false)
        timePickerDialog.show()
    }

    private fun setDate() {
        val datePickerDialog = DatePickerDialog(this, dateSetListener, myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH))
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        datePickerDialog.show()
    }

    private fun updateLabelTime() {

        val myFormat = "HH:mm"  // HH:mm:ss
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        finalTime = sdf.format(myCalendar.time)


        val myFormat2 = "h:mm a"
        val sdf2 = SimpleDateFormat(myFormat2, Locale.US)
        editSetTime?.setText(sdf2.format(myCalendar.time))

//        imgCancelTime.visibility = View.VISIBLE
    }

    private fun updateLabelDate() {

        val myFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        finalDate = sdf.format(myCalendar.time)

        val myFormat2 = "EEE, d MMM yyyy"
        val sdf2 = SimpleDateFormat(myFormat2, Locale.US)
        editSetDate?.setText(sdf2.format(myCalendar.time))

//        relativeLayoutTime.visibility = View.VISIBLE
//        imgCancelDate.visibility = View.VISIBLE
    }


}