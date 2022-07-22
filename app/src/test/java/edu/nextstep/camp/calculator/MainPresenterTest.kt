package edu.nextstep.camp.calculator

import com.google.common.truth.Truth.assertThat
import edu.nextstep.camp.calculator.domain.Expression
import edu.nextstep.camp.calculator.domain.Operator
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

/**
 * MainPresenter에 대한 유닛테스트
 * Created by jeongjinhong on 2022. 07. 22..
 */
class MainPresenterTest {

    private lateinit var presenter: MainPresenter
    private lateinit var view: MainContract.View

    @BeforeEach
    fun setUp() {
        view = mockk() // relaxUnitFun = true 요거하면 -> view 관련 함수들 nothing 처리 안해줘도 되긴함 (요건 그냥 공부용 주석)
        presenter = MainPresenter(view)
    }

    @Test
    fun `숫자가 입력되면 수식에 추가되고 변경된 수식을 보여줘야 한다`() {
        // given
        val expressionSlot = slot<Expression>()
        every { view.showExpression(capture(expressionSlot)) } answers { nothing }

        // when
        presenter.addOperand(1)

        // then
        val actual = expressionSlot.captured
        assertThat(actual.toString()).isEqualTo("1")
        verify { view.showExpression(actual) }
    }

    @Test
    fun `연산자가 입력되면 수식에 추가되고 변경된 수식을 보여줘야 한다`() {
        // given
        val expressionSlot = slot<Expression>()
        every { view.showExpression(capture(expressionSlot)) } answers { nothing }
        presenter.addOperand(1)
        // when
        presenter.addOperator(Operator.Plus)

        // then
        val actual = expressionSlot.captured
        assertThat(actual.toString()).isEqualTo("1 +")
        verify { view.showExpression(actual) }
    }

    @Test
    fun `삭제명령이 입력되면 마지막 문자가 삭제된채로 보여줘야 한다`() {
        // given
        val expressionSlot = slot<Expression>()
        every { view.showExpression(capture(expressionSlot)) } answers { nothing }
        presenter.addOperand(1)
        presenter.addOperator(Operator.Plus)
        // when
        presenter.removeLast()

        // then
        val actual = expressionSlot.captured
        assertThat(actual.toString()).isEqualTo("1")
        verify { view.showExpression(actual) }
    }

    @Test
    fun `등호가 입력되면 계산된 결과값을 보여줘야 한다`() {
        // given
        val expressionSlot = slot<Expression>()
        every { view.showExpression(capture(expressionSlot)) } answers { nothing }
        presenter.addOperand(1)
        presenter.addOperator(Operator.Plus)
        presenter.addOperand(1)
        // when
        presenter.calculateExpression()
        // then
        val actual = expressionSlot.captured
        assertThat(actual.toString()).isEqualTo("2")
        verify { view.showExpression(actual) }
    }

    @Test
    fun `수식이 완성되지 않은채로 등호가 입력되면 토스트를 보여줘야 한다`() {
        // given
        val expressionSlot = slot<Expression>()
        every { view.showExpression(capture(expressionSlot)) } answers { nothing }
        every { view.showIncompleteExpressionToast() } answers { nothing }
        presenter.addOperand(1)
        presenter.addOperator(Operator.Plus)
        // when
        presenter.calculateExpression()
        // then
        val actual = expressionSlot.captured
        assertThat(actual.toString()).isEqualTo("1 +")
        verify { view.showExpression(actual) }
    }
}