package io.univalence.ws_scala3.internal

import scala.quoted.*
import scala.reflect.ClassTag
import scala.reflect.ClassTag.Nothing

object exercise_tools {

  final private[internal] case class FileContext(file: String, path: String, line: Int, context: Seq[PartContext])
  final private[internal] case class CheckContext(expression: String, fileContext: FileContext)

  private[internal] enum CheckResultType {
    case SUCCESS, FAILURE, ERROR
  }

  private[internal] enum CheckResult {
    case Success(context: CheckContext)
    case Failure(context: CheckContext)
    case Error(context: CheckContext, exception: Throwable)

    def expression: String =
      this match {
        case Success(context)  => context.expression
        case Failure(context)  => context.expression
        case Error(context, _) => context.expression
      }

    def getType: CheckResultType =
      this match {
        case Success(_)  => CheckResultType.SUCCESS
        case Failure(_)  => CheckResultType.FAILURE
        case Error(_, _) => CheckResultType.ERROR
      }
  }

  final private[internal] case class PartContext(title: String)

  private var activatedContexts: Seq[PartContext] = Seq.empty

  final inline def ?? : Any = Nothing

  /** Check a boolean expression and display the result. */
  inline def check(inline expression: Boolean): Unit = ${ checkImpl('expression) }

  private def checkImpl(expression: Expr[Boolean])(using Quotes): Expr[Unit] = {
    val result = checkResultOfImpl(expression)

    '{
      displayCheckResult($result)
    }
  }

  private[internal] inline def checkResultOf(inline expression: Boolean): CheckResult =
    ${ checkResultOfImpl('expression) }

  private def checkResultOfImpl(expression: Expr[Boolean])(using Quotes): Expr[CheckResult] = {
    import quotes.reflect.*

    val fileContext = captureFileContext(expression)

    val term = expression.asTerm.underlyingArgument

    val position   = term.pos
    val sourceCode = position.sourceCode.getOrElse("")
    val exprBody   = Expr(sourceCode.replace("\n", "\n\t"))

    '{
      val context = CheckContext($exprBody, $fileContext)
      evaluate($expression, context)
    }
  }

  private def captureFileContext[A](content: Expr[A])(using Quotes): Expr[FileContext] = {
    import quotes.reflect
    import quotes.reflect.*

    val sourceFilename = Expr(reflect.SourceFile.current.name)
    val position       = content.asTerm.underlyingArgument.pos
    val path           = Expr(position.sourceFile.path)
    val line           = Expr(position.startLine + 1)

    '{ FileContext(file = $sourceFilename, path = $path, line = $line, context = activatedContexts) }
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
  inline def exercise(title: String, activated: Boolean)(content: => Unit): Unit =
    inline if (activated) {
      activatedContexts = activatedContexts :+ PartContext(title)
      part(activatedContexts.map(_.title).mkString(" > "))

      try content
      catch {
        case PartException(l, c) =>
          throw PartException(s"$title > $l", c)
        case e: Exception =>
          throw new PartException(title, e)
      } finally activatedContexts = activatedContexts.init
    } else {
      part((activatedContexts.map(_.title) :+ s"$title ${Console.RED}(TO ACTIVATE)").mkString(" > "))
    }

  inline def section(label: String)(f: => Unit): Unit = exercise(label, true)(f)

  final case class PartException(label: String, cause: Throwable)
      extends RuntimeException(s"""Exception caught in part "$label"""", cause)

  inline def exercisePart(title: String): Unit = part((activatedContexts.map(_.title) :+ title).mkString(" > "))

  /** Display a part with a file name. */
  inline def part(inline title: String): Unit = ${ partImpl('title) }

  private def partImpl(title: Expr[String])(using Quotes): Expr[Unit] = {
    val fileContext = captureFileContext(title)

    '{
      println(s"${Console.BOLD}${Console.YELLOW}${$fileContext.file}:${$fileContext.line} - ${$title}${Console.RESET}")
    }
  }

  private def evaluate(expression: => Boolean, checkContext: CheckContext): CheckResult =
    scala.util.Try(expression) match {
      case scala.util.Success(true)      => CheckResult.Success(checkContext)
      case scala.util.Success(false)     => CheckResult.Failure(checkContext)
      case scala.util.Failure(exception) => CheckResult.Error(checkContext, exception)
    }

  private def displayCheckResult(result: CheckResult): Unit = {
    val resultDisplay =
      result match {
        case CheckResult.Success(context) =>
          s"${Console.GREEN}OK (line:${context.fileContext.line})${Console.RESET}"
        case CheckResult.Failure(context) =>
          s"${Console.YELLOW}FAILED (${context.fileContext.path}:${context.fileContext.line})${Console.RESET}"
        case CheckResult.Error(context, e) =>
          s"${Console.RED}ERROR (${context.fileContext.path}:${context.fileContext.line}: ${e.getClass.getCanonicalName}: ${e.getMessage})${Console.RESET}"
      }

    println(("\t" * activatedContexts.size) + s">>> ${result.expression} $resultDisplay")
  }

}
