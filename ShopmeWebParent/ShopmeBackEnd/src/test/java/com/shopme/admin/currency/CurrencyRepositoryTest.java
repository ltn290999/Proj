package com.shopme.admin.currency;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.admin.setting.CurrencyRepository;
import com.shopme.common.entity.Currency;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class CurrencyRepositoryTest {

    @Autowired
    private CurrencyRepository repo;
    
    @Test
    public void testCreateCurrencies() {
	repo.save(new Currency("United States Dollar","$","USB"));
	repo.save(new Currency("British Pound","£","GBP"));
	repo.save(new Currency("Japanese Yen","¥","JPY"));
	repo.save(new Currency("Euro","€","EUR"));
	repo.save(new Currency("Russian Ruble","₽","RUB"));
	repo.save(new Currency("South Korean Won","₩","KRW"));
	repo.save(new Currency("Chinese Yuan","¥","CNY"));
	repo.save(new Currency("Brazilian Real","R$","BRL"));
	repo.save(new Currency("Australian Dollar","$","AUD"));
	repo.save(new Currency("Canadian Dollar","$","CAD"));
	repo.save(new Currency("Vietnamese đồng","đ","VND"));
	repo.save(new Currency("Indian Rupee","₹","INR"));
    }
}
