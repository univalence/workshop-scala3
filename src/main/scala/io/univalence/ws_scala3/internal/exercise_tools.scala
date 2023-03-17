/*
 * Copyright 2022 Univalence
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.univalence.ws_scala3.internal

import scala.quoted.*
import scala.reflect.ClassTag
import scala.reflect.ClassTag.Nothing

object exercise_tools {

  final private[internal] case class FileContext(file: String, path: String, line: Int, context: Seq[PartContext])
  final private[internal] case class CheckContext(expression: String, fileContext: FileContext)

  private[internal] enum CheckResultType {
    case SUCCESS, FAILURE, ERROR, TODO
  }

  val COLOR_GREY = "\u001b[38;5;8m"
  val ITALIC     = "\u001b[3m"
  val UNBOLD     = "\u001b[22m"

  private[internal] enum CheckResult {
    case Success(context: CheckContext)
    case Failure(context: CheckContext)
    case Error(context: CheckContext, exception: Throwable)
    case Todo(context: CheckContext)

    def expression: String =
      this match {
        case Success(context)  => context.expression
        case Failure(context)  => context.expression
        case Error(context, _) => context.expression
        case Todo(context)     => context.expression
      }

    def getType: CheckResultType =
      this match {
        case Success(_)  => CheckResultType.SUCCESS
        case Failure(_)  => CheckResultType.FAILURE
        case Error(_, _) => CheckResultType.ERROR
        case Todo(_)     => CheckResultType.TODO
      }
  }

  def partIndent: String = "\t" * activatedContexts.size

  case class Part(path: String, line: Int, label: String) {
    def reportOnly(f: => Unit): Unit = {
      activatedContexts = activatedContexts :+ PartContext(label)
      val content = activatedContexts.map(_.title).mkString(" > ")
      println(s"${Console.YELLOW}+++ $content ${Console.RED}${Console.BOLD}(TO ACTIVATE)${Console.RESET} ($path:$line)")
      activatedContexts = activatedContexts.init
    }

    def run(f: => Unit): Unit = {
      activatedContexts = activatedContexts :+ PartContext(label)
      val content = activatedContexts.map(_.title).mkString(" > ")

      try {
        println(s"${Console.YELLOW}+++ $content ($path:$line)${Console.RESET}")

        f
      } catch {
        case PartException(l, c) =>
          throw PartException(s"$label > $l", c)
        case e: NotImplementedError =>
          println(
            partIndent + s">>> ${Console.CYAN}${Console.BOLD}TODO$UNBOLD an implementation is missing.${Console.RESET} ($path:$line)"
          )
        case e: Exception =>
          throw PartException(label, e)
      } finally activatedContexts = activatedContexts.init
    }
  }

  final private[internal] case class PartContext(title: String)

  private var activatedContexts: Seq[PartContext] = Seq.empty

  /** value place holder */
  final inline def ?? : Any = Nothing

  /** function body place holder */
  final inline def |>? : Nothing = throw new NotImplementedError

  /** type place holder */
  final type !? = Nothing

  /** Check a boolean expression and display the result. */
  inline def check(inline expression: Boolean): Unit = ${ checkImpl('{ expression }) }

  private def checkImpl(expression: Expr[Boolean])(using Quotes): Expr[Unit] = {
    val result = checkResultOfImpl(expression)

    '{
      displayCheckResult($result)
    }
  }

  private[internal] inline def checkResultOf(expression: Boolean): CheckResult = ${ checkResultOfImpl('{ expression }) }

  private def checkResultOfImpl(expression: Expr[Boolean])(using Quotes): Expr[CheckResult] = {
    import quotes.reflect.*

    val fileContext = captureFileContext(expression)

    val term = expression.asTerm.underlyingArgument

    val position   = term.pos
    val sourceCode = position.sourceCode.getOrElse("")
    val exprBody =
      Expr(
        sourceCode
          .replace("\n", "\n\t")
          .replaceFirst("(//[^\n]+)\n", COLOR_GREY + "$$1" + Console.RESET + "\n")
      )

    '{
      evaluate($expression, CheckContext($exprBody, $fileContext))
    }
  }

  /**
   * Create an exercise.
   *
   * @param title
   *   simple label
   * @param activated
   *   indicate if the code of the exercise is activated
   * @param content
   *   code of the exercise
   */
  inline def exercise(inline title: String, inline activated: Boolean)(content: => Unit): Unit =
    ${ exerciseImpl('{ title }, '{ activated })('{ content }) }

  private def exerciseImpl(title: Expr[String], activated: Expr[Boolean])(
      content: Expr[Unit]
  )(using Quotes): Expr[Unit] = {
    import quotes.reflect.*

//    quotes.reflect.report.info(">>> exerciseImpl <<<")

    val fileContext = captureFileContext(title)

    if (activated.valueOrAbort)
      '{
        Part(
          path  = $fileContext.path,
          line  = $fileContext.line,
          label = $title
        ).run($content)
      }
    else
      '{
        Part(
          path  = $fileContext.path,
          line  = $fileContext.line,
          label = $title
        ).reportOnly($content)
      }
  }

  inline def section(inline label: String)(content: => Unit): Unit = ${ sectionImpl('{ label })('{ content }) }

  def sectionImpl(label: Expr[String])(content: Expr[Unit])(using Quotes): Expr[Unit] = {
    import quotes.reflect.*

//    quotes.reflect.report.info(">>> sectionImpl <<<")

    val fileContext = captureFileContext(label)

    '{
      Part(
        path  = $fileContext.path,
        line  = $fileContext.line,
        label = $label
      ).run($content)
    }
  }

  final case class PartException(label: String, cause: Throwable)
      extends RuntimeException(s"""Exception caught in part "$label"""", cause)

  inline def exercisePart(title: String): Unit = part((activatedContexts.map(_.title) :+ title).mkString(" > "))

  inline def comment(inline content: String): Unit = ${ commentImpl('{ content }) }

  private def commentImpl(content: Expr[String])(using Quotes): Expr[Unit] = {
    import quotes.reflect.*

//    quotes.reflect.report.info(">>> commentImpl <<<")

    val fileContext = captureFileContext(content)

    '{
      println(
        ("\t" * activatedContexts.size) + s"$ITALIC$COLOR_GREY# ${$content} ${Console.WHITE}(line:${$fileContext.line})${Console.RESET}"
      )
    }
  }

  /** Display a part with a file name. */
  inline def part(inline title: String): Unit = ${ partImpl('{ title }) }

  private def partImpl(title: Expr[String])(using Quotes): Expr[Unit] = {
    quotes.reflect.report.info(">>> partImpl <<<")

    val fileContext = captureFileContext(title)

    '{
      println(s"${Console.YELLOW}${$fileContext.path}:${$fileContext.line} - ${$title}${Console.RESET}")
    }
  }

  private def evaluate(expression: => Boolean, checkContext: CheckContext): CheckResult =
    scala.util.Try(expression) match {
      case scala.util.Success(true)  => CheckResult.Success(checkContext)
      case scala.util.Success(false) => CheckResult.Failure(checkContext)
      case scala.util.Failure(exception) =>
        exception match {
          case _: NotImplementedError => CheckResult.Todo(checkContext)
          case _                      => CheckResult.Error(checkContext, exception)
        }
    }

  private def displayCheckResult(result: CheckResult): Unit = {
    val resultDisplay =
      result match {
        case CheckResult.Success(context) =>
          s"${Console.GREEN}${Console.BOLD}OK$UNBOLD (line:${context.fileContext.line})${Console.RESET}"
        case CheckResult.Failure(context) =>
          s"${Console.YELLOW}${Console.BOLD}FAILED$UNBOLD (${context.fileContext.path}:${context.fileContext.line})${Console.RESET}"
        case CheckResult.Todo(context) =>
          s"${Console.CYAN}${Console.BOLD}TODO$UNBOLD missing implementation detected while calling${Console.RESET} ${result.expression} (${context.fileContext.path}:${context.fileContext.line})"
        case CheckResult.Error(context, e) =>
          s"${Console.RED}${Console.BOLD}ERROR$UNBOLD (${context.fileContext.path}:${context.fileContext.line}: ${e.getClass.getCanonicalName}: ${e.getMessage})${Console.RESET}"
      }

    println(("\t" * activatedContexts.size) + s">>> ${Console.BOLD}${result.expression}${Console.RESET} $resultDisplay")
  }

  private def captureFileContext[A](content: Expr[A])(using Quotes): Expr[FileContext] = {
    import quotes.reflect
    import quotes.reflect.*

    val sourceFilename = Expr(reflect.SourceFile.current.name)
    val position       = content.asTerm.underlyingArgument.pos
    val path           = Expr(reflect.SourceFile.current.path)
    val line           = Expr(position.startLine + 1)

    '{ FileContext(file = $sourceFilename, path = $path, line = $line, context = activatedContexts) }
  }
}
