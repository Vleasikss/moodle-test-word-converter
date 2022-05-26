package org.example

case class MoodleAnswer(
  isCorrect: Option[Boolean],
  isSelected: Boolean,
  label: String,
  number: String,
)

case class MoodleTestInfo(
  name: String,
  prompt: String,
  answers: List[MoodleAnswer],
)

case class MoodleTestGrade(
  grade: Double,
  maxGrade: Double
)

case class MoodleTest(
  number: Int,
  info: MoodleTestInfo,
  grade: MoodleTestGrade
)

trait Report

case class MoodleTestReport(
  username: String,
  id: String,
  title: String,
  tests: List[MoodleTest]
) extends Report