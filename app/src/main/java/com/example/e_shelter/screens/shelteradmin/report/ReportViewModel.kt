package com.example.e_shelter.screens.shelteradmin.report

import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Environment
import androidx.lifecycle.ViewModel
import com.example.e_shelter.App
import com.example.e_shelter.convertLongToDateString
import com.example.e_shelter.database.entities.Animal
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ReportViewModel : ViewModel() {
    private val database = App.database.eShelterDatabaseDao
    private val firebaseAuth = App.firebaseAuth
    var dogs: List<Animal>
    var cats: List<Animal>
    var exotic: List<Animal>
    private val currentUser =  database.getUser(firebaseAuth.user!!.uid)

    init {
        dogs = database.getAllAnimalsBySpeciesList("Собака", currentUser!!.shelterId!!)
        cats = database.getAllAnimalsBySpeciesList("Кот", currentUser.shelterId!!)
        exotic = database.getAllExoticAnimalsList("Собака", "Кот", currentUser.shelterId!!)
    }


    fun onGetReport(dateFrom: Long, dateTo: Long): Boolean {
        val animals = database.getAnimalsFoundHomeInDatePeriod(dateFrom, dateTo, currentUser!!.shelterId!!)

        val paint = Paint()
        paint.color = Color.BLACK
        paint.style = Paint.Style.FILL
        paint.textSize = 18f

        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            "Отчёт о деятельности приюта.pdf"
        )
        val document = PdfDocument()

        try {
            FileOutputStream(file).use { out ->
                val pageInfo = PdfDocument.PageInfo.Builder(700, 1000, 1).create()
                val page = document.startPage(pageInfo)
                val c = page.canvas
                var y = 50f
                val x = 50f
                val step = 36f

                c.drawText("Период: " + convertLongToDateString(dateFrom) + " - " + convertLongToDateString(dateTo), 2*x, y, paint)
                c.drawText("Сейчас в приюте находится: ", 2*x, step.let { y += it; y }, paint )
                c.drawText("Кошек: ".plus(getAnimalsNum(cats, "Ждёт семью")), x, step.let { y += it; y }, paint)
                c.drawText("Собак: ".plus(getAnimalsNum(dogs, "Ждёт семью")), x, step.let { y += it; y }, paint)
                c.drawText("Экзотов: ".plus(getAnimalsNum(exotic, "Ждёт семью")), x, step.let { y += it; y }, paint)

                c.drawText("На передержке находится: ", 2*x, step.let { y += it; y }, paint )
                c.drawText("Кошек: ".plus(getAnimalsNum(cats, "На передержке")), x, step.let { y += it; y }, paint)
                c.drawText("Собак: ".plus(getAnimalsNum(dogs, "На передержке")), x, step.let { y += it; y }, paint)
                c.drawText("Экзотов: ".plus(getAnimalsNum(exotic, "На передержке")), x, step.let { y += it; y }, paint)


                if (animals.isNotEmpty()) {
                    y += 100f
                    c.drawText("За отчётный период были пристроены в семью: ", 2 * x, y, paint)
                    var num = 1

                    for (animal in animals) {
                        c.drawText(num.toString().plus(". Кличка: ").plus(animal.name).plus(". Вид: ").plus(animal.species).plus("  "), x, step.let { y += it; y }, paint)
                        c.drawText(("Дата пристройства в семью: ").plus(convertLongToDateString(animal.foundHomeDate!!)), x, step.let { y += it; y }, paint)
                        num++
                    }
                }
                c.save()
                document.finishPage(page)
                document.writeTo(out)
                out.flush()
                return true
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            document.close()
        }
        return false
    }

    private fun getAnimalsNum(animals: List<Animal>, status: String): Int {
        var count = 0

        if (animals.isNotEmpty()) {
            for (animal in animals) {
                if (animal.shelterId == currentUser!!.shelterId && animal.status == status) count++
            }
        }
        return count
    }
}