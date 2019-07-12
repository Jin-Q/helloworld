import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LoanTest {

	public static void main(String[] args) {
		args = new String[]{"480000","20","4.9","2016","1"};
		System.out
				.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		System.out.println("等额本金还贷【 总计贷款：" + args[0] + "（元）；偿还年数：" + args[1]
				+ "（年）；年利息：" + args[2] + "】");
		System.out
				.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
		BigDecimal totelYearOfMortgage = new BigDecimal(args[1]);
		BigDecimal totelMortgage = new BigDecimal(args[0]);
		BigDecimal monthsOfYear = new BigDecimal(12);
		BigDecimal perMonthMortgage = totelMortgage.divide(totelYearOfMortgage
				.multiply(monthsOfYear), 10, BigDecimal.ROUND_HALF_UP);

		// do change start
		int yearStart = Integer.parseInt(args[3]);
		int monthStart = Integer.parseInt(args[4]);
		BigDecimal rateMortgage = new BigDecimal(args[2]).divide(
				new BigDecimal(100), 10, BigDecimal.ROUND_HALF_UP);
		;
		// do change end

		int allMonth = totelYearOfMortgage.multiply(monthsOfYear).intValue();
		BigDecimal totelMortgagePay = new BigDecimal(0);
		int index = 0;

		for (int monthIndex = monthStart; monthIndex < allMonth + monthStart; monthIndex++, index++) {
			int currYear = 0;
			int currMonth = 0;
			if (monthIndex % monthsOfYear.intValue() == 0) {
				currYear = yearStart + (monthIndex / monthsOfYear.intValue())
						- 1;
				currMonth = monthsOfYear.intValue();
			} else {
				currYear = yearStart + (monthIndex / monthsOfYear.intValue());
				currMonth = monthIndex % monthsOfYear.intValue();
			}
			BigDecimal currMonthMortgageTotelPrincipal = totelMortgage
					.subtract(perMonthMortgage.multiply(new BigDecimal(index)));

			BigDecimal currMonthMortgageRatePay = (currMonthMortgageTotelPrincipal
					.multiply(rateMortgage).divide(monthsOfYear, 2,
					BigDecimal.ROUND_HALF_UP));
			BigDecimal currMonthMortgageAllPay = currMonthMortgageRatePay
					.add(perMonthMortgage);
			totelMortgagePay = totelMortgagePay.add(currMonthMortgageAllPay);

		}

		BigDecimal currTotelMortgagePay = new BigDecimal(0);
		;
		index = 0;
		for (int monthIndex = monthStart; monthIndex < allMonth + monthStart; monthIndex++, index++) {
			int currYear = 0;
			int currMonth = 0;
			if (monthIndex % monthsOfYear.intValue() == 0) {
				currYear = yearStart + (monthIndex / monthsOfYear.intValue())
						- 1;
				currMonth = monthsOfYear.intValue();
			} else {
				currYear = yearStart + (monthIndex / monthsOfYear.intValue());
				currMonth = monthIndex % monthsOfYear.intValue();
			}
			Date nowDate = new Date();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
			String[] strYearMonth = sf.format(nowDate).split("-");
			int sysYear = Integer.parseInt(strYearMonth[0]);
			int sysMonth = Integer.parseInt(strYearMonth[1]);
			if (currYear == sysYear && sysMonth == currMonth) {
				System.out
						.println("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			}
			BigDecimal currMonthMortgageTotelPrincipal = totelMortgage
					.subtract(perMonthMortgage.multiply(new BigDecimal(index)));
			BigDecimal currMonthMortgageTotelPrincipalTmp = totelMortgage
					.subtract(
							perMonthMortgage
									.multiply(new BigDecimal(index + 1)))
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal currMonthMortgageRatePay = (currMonthMortgageTotelPrincipal
					.multiply(rateMortgage).divide(monthsOfYear, 2,
					BigDecimal.ROUND_HALF_UP));
			BigDecimal currMonthMortgageAllPay = currMonthMortgageRatePay
					.add(perMonthMortgage);
			BigDecimal currMonthMortgageAllPayTmp = currMonthMortgageAllPay
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			currTotelMortgagePay = currTotelMortgagePay
					.add(currMonthMortgageAllPay);
			BigDecimal currMonthMortgageRatePayTmp = currMonthMortgageRatePay
					.setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal leftMonthMortgageRatePay = totelMortgagePay
					.subtract(currTotelMortgagePay);
			leftMonthMortgageRatePay = leftMonthMortgageRatePay.setScale(2,
					BigDecimal.ROUND_HALF_UP);
			System.out.print(currYear + "年"
					+ (currMonth > 9 ? currMonth : "0" + currMonth)
					+ "月  【需要还贷金额 : " + currMonthMortgageAllPayTmp
					+ "（元）; 其中偿还的利息 ： " + currMonthMortgageRatePayTmp
					+ "（元）; 剩余还贷总金额 : " + leftMonthMortgageRatePay + "（元）");
			System.out.println("; 剩余还贷总本金 : "
					+ currMonthMortgageTotelPrincipalTmp + "（元）】");
		}
		System.out.println("还贷总金额 : " + totelMortgagePay + "（元）");
	}
}
