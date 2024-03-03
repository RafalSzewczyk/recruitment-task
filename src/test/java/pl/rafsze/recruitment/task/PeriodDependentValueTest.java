package pl.rafsze.recruitment.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import pl.rafsze.recruitment.task.PeriodDependentValue;
import pl.rafsze.recruitment.task.PeriodDependentValue.PeriodDependentValueBuilder;

public class PeriodDependentValueTest
{
	@Test
	public void testPeriodDependentValues()
	{
		PeriodDependentValue<String> parameter = PeriodDependentValue.<String>builder()
				.putValue(0, 0, "0")
				.putValue(1, 2, "1-2")
				.putValue(3, 6, "3-6")
				.putValue(8, 12, "8-12")
				.build();
				
		assertEquals("0", parameter.getValue(0));
		assertEquals("1-2", parameter.getValue(2));
		assertEquals("3-6", parameter.getValue(3));
		assertEquals("3-6", parameter.getValue(5));
		assertEquals("3-6", parameter.getValue(6));
		assertEquals(null, parameter.getValue(7));
		assertEquals("8-12", parameter.getValue(8));
		assertEquals("8-12", parameter.getValue(12));
		assertEquals(null, parameter.getValue(13));
	}
	
	@Test
	public void testBuilderPutValueOverlapPeriods()
	{
		PeriodDependentValueBuilder<String> builder = PeriodDependentValue.builder();
		
		builder.putValue(0, 6, "0-6");
		builder.putValue(7, 10, "7-10");
		
		assertThrows(IllegalArgumentException.class, () -> builder.putValue(8, 12, "8-12"));
		assertThrows(IllegalArgumentException.class, () -> builder.putValue(2, 3, "2-3"));
		assertThrows(IllegalArgumentException.class, () -> builder.putValue(4, 8, "4-8"));
	}
}
