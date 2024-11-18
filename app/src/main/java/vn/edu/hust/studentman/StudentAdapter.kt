import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import vn.edu.hust.studentman.R
import vn.edu.hust.studentman.StudentModel

class StudentAdapter(val students: MutableList<StudentModel>): RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

  class StudentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val textStudentName: TextView = itemView.findViewById(R.id.text_student_name)
    val textStudentId: TextView = itemView.findViewById(R.id.text_student_id)
    val imageEdit: ImageView = itemView.findViewById(R.id.image_edit)
    val imageRemove: ImageView = itemView.findViewById(R.id.image_remove)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
    val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_student_item, parent, false)
    return StudentViewHolder(itemView)
  }

  override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
    val student = students[position]

    // Hiển thị thông tin sinh viên
    holder.textStudentName.text = student.studentName
    holder.textStudentId.text = student.studentId

    // Xử lý sự kiện cho nút Edit
    holder.imageEdit.setOnClickListener {
      val context = holder.itemView.context
      val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_student, null)

      val dialog = AlertDialog.Builder(context)
        .setTitle("Edit Student")
        .setView(dialogView)
        .setPositiveButton("Save") { _, _ ->
          val newName = dialogView.findViewById<EditText>(R.id.edit_text_name).text.toString()
          val newId = dialogView.findViewById<EditText>(R.id.edit_text_id).text.toString()

          if (newName.isNotBlank() && newId.isNotBlank()) {
            // Cập nhật thông tin sinh viên
            students[position] = StudentModel(newName, newId)
            notifyItemChanged(position) // Thông báo RecyclerView cập nhật
          } else {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
          }
        }
        .setNegativeButton("Cancel", null)
        .create()

      // Hiển thị thông tin hiện tại của sinh viên
      dialogView.findViewById<EditText>(R.id.edit_text_name).setText(student.studentName)
      dialogView.findViewById<EditText>(R.id.edit_text_id).setText(student.studentId)

      dialog.show()
    }

    // Xử lý sự kiện cho nút Delete
    holder.imageRemove.setOnClickListener {
      val context = holder.itemView.context
      val removedStudent = students[position]
      val removedPosition = position

      AlertDialog.Builder(context)
        .setTitle("Delete Student")
        .setMessage("Are you sure you want to delete ${removedStudent.studentName}?")
        .setPositiveButton("Delete") { _, _ ->
          // Xóa sinh viên khỏi danh sách
          students.removeAt(position)
          notifyItemRemoved(position)

          // Hiển thị Snackbar để Undo
          Snackbar.make(holder.itemView, "${removedStudent.studentName} deleted", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
              students.add(removedPosition, removedStudent)
              notifyItemInserted(removedPosition)
            }
            .show()
        }
        .setNegativeButton("Cancel", null)
        .create()
        .show()
    }
  }

  override fun getItemCount(): Int = students.size
}
