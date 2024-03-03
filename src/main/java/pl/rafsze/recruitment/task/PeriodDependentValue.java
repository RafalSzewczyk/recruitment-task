package pl.rafsze.recruitment.task;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PeriodDependentValue <T>
{
	private final List<Entry<T>> values;
	
	public T getValue(int period)
	{
		return values.stream()
				.filter(e -> period >= e.getFrom() && period <= e.getTo())
				.findFirst()
				.map(Entry::getValue)
				.orElse(null);
	}
	
	public List<T> getValues()
	{
		return values.stream().map(Entry::getValue).collect(Collectors.toList());
	}
	
	public static <T> PeriodDependentValueBuilder<T> builder()
	{
		return new PeriodDependentValueBuilder<>();
	}
	
	@NoArgsConstructor(access = AccessLevel.PRIVATE)
	public static class PeriodDependentValueBuilder <T>
	{
		private final List<Entry<T>> values = new ArrayList<>();
		
		public PeriodDependentValue<T> build()
		{
			return new PeriodDependentValue<>(values);
		}
		
		public PeriodDependentValueBuilder<T> putValue(int from, int to, T value)
		{
			if ( from > to )
			{
				throw new IllegalArgumentException("Invalid range - from > to");
			}
			
			checkOverlap(from, to);
			values.add(new Entry<>(from, to, value));
			return this;
		}
		
		private void checkOverlap(int from, int to)
		{
			boolean overlappingPeriods = values.stream()
					.anyMatch(e -> from <= e.getTo() && to >= e.getFrom());
			
			if ( overlappingPeriods )
			{
				throw new IllegalArgumentException("Unable to put value - overlapping periods");
			}
		}
	}
	
	@Data
	public static class Entry <T>
	{
		private final int from;
		private final int to;
		private final T value;
	}
	
}
