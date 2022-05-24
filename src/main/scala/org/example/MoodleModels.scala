package org.example

case class MoodleAnswer(
  isCorrect: Option[Boolean],
  isSelected: Boolean,
  label: String,
  number: String,
)

case class MoodleTest(
  name: String,
  prompt: String,
  testNumber: Int,
  answers: List[MoodleAnswer],
)

case class MoodleTestGrade(
  id: String,
  testNumber: Int,
  grade: Double,
  maxGrade: Double
)

case class MoodleTestReport(
  username: String,
  id: String,
  title: String,
  tests: List[(MoodleTestGrade, MoodleTest)]
)