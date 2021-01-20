package model.services;

import model.entities.Contract;
import model.entities.Installment;

import java.util.Calendar;
import java.util.Date;

public class ContractService {

    private OnlinePaymentService onlinePaymentService;

    public ContractService(OnlinePaymentService onlinePaymentService) {
        this.onlinePaymentService = onlinePaymentService;
    }

    public void processContract(Contract contract, int months) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(contract.getDate());
        double baseValue = contract.getTotalValue() / months;

        for (int i = 1; i <= months; i++) {
            double updatedValue = baseValue + onlinePaymentService.interest(baseValue, i);
            double finalValue = updatedValue + onlinePaymentService.paymentFee(updatedValue);
            cal.add(Calendar.MONTH, 1);
            Date date = cal.getTime();
            contract.getInstallments().add(new Installment(date, finalValue));
        }
    }
}
