package io.univalence.ws_scala3.internal

import exercise_tools.*
import munit.FunSuite

class exercise_toolsTest extends FunSuite {
  test("check should return success on true expression") {
    val result = checkResultOf(1 + 1 == 2)

    assertEquals(result.getType, CheckResultType.SUCCESS)
    assertEquals(result.expression, "1 + 1 == 2")
  }
  test("check should return failure on false expression") {
    val result = checkResultOf(1 + 1 == 11)

    assertEquals(result.getType, CheckResultType.FAILURE)
    assertEquals(result.expression, "1 + 1 == 11")
  }
  test("check should return error on exception") {
    val result = checkResultOf(throw new IllegalArgumentException("woops"))

    val CheckResult.Error(_, exception) = result

    assertEquals(result.expression, "throw new IllegalArgumentException(\"woops\")")
    assert(exception.isInstanceOf[IllegalArgumentException], "exception should be of type IllegalArgumentException")
  }
}
