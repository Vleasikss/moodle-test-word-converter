package org.example

import com.spire.doc.Document
import com.spire.doc.fields.TextRange

import java.awt.Color

package object writer {

  private[writer] lazy val GREEN = new Color(0, 255, 0)
  private[writer] lazy val RED = new Color(255, 0, 0)
  private[writer] lazy val BLUE = new Color(0, 64, 250)

  private[writer] implicit class TextRangeImplicits(range: TextRange) {
    def appendAnswerStyle(isCorrect: Option[Boolean], isSelected: Boolean)(implicit document: Document): TextRange = {
      val color: Option[Color] = isCorrect match {
        case Some(value) => if (value) Option(GREEN) else Option(RED)
        case _ => if (isSelected) Option(BLUE) else None
      }

      range.getCharacterFormat.setFontName("Times New Roman")
      range.getCharacterFormat.setFontSize(16f)
      color.foreach(range.getCharacterFormat.setTextColor)
      range
    }

    def appendGradeStyle(implicit document: Document): TextRange = {
      range.getCharacterFormat.setItalic(true)
      range.getCharacterFormat.setBold(false)
      range
    }

  }

}
