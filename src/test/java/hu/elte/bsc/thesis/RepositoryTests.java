package hu.elte.bsc.thesis;

import hu.elte.bsc.thesis.model.*;
import hu.elte.bsc.thesis.repository.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RunWith(SpringRunner.class)
@DataJpaTest
public class RepositoryTests {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CompanyItemRepository companyItemRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private RegistrationCodeRepository registrationCodeRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private StoreItemRepository storeItemRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void findUserByUsername(){
        User user = createTestUser(User.Role.ADMIN);
        entityManager.persist(user);
        entityManager.flush();
        Optional<User> optUser = userRepository.findByUsername("testUsername");
        optUser.ifPresent(u-> assertThat(u.getUsername()).isEqualTo(user.getUsername()));
    }

    @Test
    public void login(){
        User user = createTestUser(User.Role.ADMIN);
        entityManager.persist(user);
        entityManager.flush();
        Optional<User> optUser = userRepository.findByUsernameAndPassword("testUsername","testPW");
        optUser.ifPresent(u-> assertThat(u.getId()).isEqualTo(user.getId()));
    }

    @Test
    public void findMaxIdIfNoEntity(){
        saleRepository.deleteAll();
        assertThat(saleRepository.findMaxId().orElse(0L)).isEqualTo(0L);
    }
    @Test
    public void findMaxIdWithEntity(){
        saleRepository.deleteAll();
        User user = createTestUser(User.Role.COMPANY_ADMIN);
        Item item = createItem();
        Company company = createCompany();
        company.getUsers().add(user);
        entityManager.persist(company);
        entityManager.flush();
        CompanyItem companyItem = createCompanyItem(company,item);
        Store store = createStore(company);
        StoreItem storeItem = createStoreItem(store,companyItem);
        Sale sale =createTestSale(user,storeItem);
        assertThat(saleRepository.findMaxId().orElse(0L)).isEqualTo(-2L);
    }
    @Test
    public void findCompanyByUserId(){
        User user = createTestUser(User.Role.COMPANY_ADMIN);
        Company company = createCompany();
        company.getUsers().add(user);
        entityManager.persist(company);
        entityManager.flush();
        Optional<Company> optCompany = companyRepository.findByUserId(-2L);
        optCompany.ifPresent(c -> assertThat(c.getId()).isEqualTo(company.getId()));
    }
    @Test
    public void findAllSalesBetweenAtCompany(){
        saleRepository.deleteAll();
        User user = createTestUser(User.Role.COMPANY_ADMIN);
        Item item = createItem();
        Company company = createCompany();
        company.getUsers().add(user);
        entityManager.persist(company);
        entityManager.flush();
        CompanyItem companyItem = createCompanyItem(company,item);
        Store store = createStore(company);
        StoreItem storeItem = createStoreItem(store,companyItem);
        Sale sale =createTestSale(user,storeItem);
        ArrayList<Sale> sales = (ArrayList<Sale>) saleRepository.findAllBetweenAtCompany(new Date(2010,01,01),new Date(2010,01,03),-2L);
        assertThat(sales.size()).isEqualTo(1);
    }
    @Test
    public void findNoSalesBetweenAtCompany(){
        saleRepository.deleteAll();
        User user = createTestUser(User.Role.COMPANY_ADMIN);
        Item item = createItem();
        Company company = createCompany();
        company.getUsers().add(user);
        entityManager.persist(company);
        entityManager.flush();
        CompanyItem companyItem = createCompanyItem(company,item);
        Store store = createStore(company);
        StoreItem storeItem = createStoreItem(store,companyItem);
        Sale sale =createTestSale(user,storeItem);
        ArrayList<Sale> sales = (ArrayList<Sale>) saleRepository.findAllBetweenAtCompany(new Date(2011,01,01),new Date(2011,01,03),-2L);
        assertThat(sales.size()).isEqualTo(0);
    }
    @Test
    public void findAllSalesBetweenAtStore(){
        saleRepository.deleteAll();
        User user = createTestUser(User.Role.COMPANY_ADMIN);
        Item item = createItem();
        Company company = createCompany();
        company.getUsers().add(user);
        entityManager.persist(company);
        entityManager.flush();
        CompanyItem companyItem = createCompanyItem(company,item);
        Store store = createStore(company);
        StoreItem storeItem = createStoreItem(store,companyItem);
        Sale sale =createTestSale(user,storeItem);
        ArrayList<Sale> sales = (ArrayList<Sale>) saleRepository.findAllBetweenAtStore(new Date(2010,01,01),new Date(2010,01,03),-2L);
        assertThat(sales.size()).isEqualTo(1);
    }
    @Test
    public void findNoSalesBetweenAtStore(){
        saleRepository.deleteAll();
        User user = createTestUser(User.Role.COMPANY_ADMIN);
        Item item = createItem();
        Company company = createCompany();
        company.getUsers().add(user);
        entityManager.persist(company);
        entityManager.flush();
        CompanyItem companyItem = createCompanyItem(company,item);
        Store store = createStore(company);
        StoreItem storeItem = createStoreItem(store,companyItem);
        Sale sale =createTestSale(user,storeItem);
        ArrayList<Sale> sales = (ArrayList<Sale>) saleRepository.findAllBetweenAtStore(new Date(2011,01,01),new Date(2011,01,03),-2L);
        assertThat(sales.size()).isEqualTo(0);
    }

    private User createTestUser(User.Role role){
        User user = new User("testUsername", "test@email.hu",
                "testPW", "testRegcode",
                new HashSet<>(), new HashSet<>(),null, role);
        user.setId(-2L);
        entityManager.persist(user);
        entityManager.flush();
        return user;
    }
    private Sale createTestSale(User user, StoreItem storeItem){
        Sale sale = new Sale(storeItem,1 ,5000L, user, new Date(2010,01,02));
        sale.setId(-2L);
        entityManager.persist(sale);
        entityManager.flush();
        return sale;
    }
    private Item createItem(){
        Item item = new Item("testBarcode",new HashSet<>());
        item.setId(-2L);
        entityManager.persist(item);
        entityManager.flush();
        return item;
    }
    private Company createCompany(){
        Company company = new Company("testCompanyName",new HashSet<>(), new HashSet<>(), new HashSet<>());
        company.setId(-2L);
        entityManager.persist(company);
        entityManager.flush();
        return company;
    }
    private CompanyItem createCompanyItem(Company company,Item item){
        CompanyItem companyItem=new CompanyItem(company,item,new HashSet<>(),"testItemName", 3000,"testDescription","");
        companyItem.setId(-2L);
        entityManager.persist(companyItem);
        entityManager.flush();
        return companyItem;
    }
    private Store createStore(Company company){
        Store store = new Store("testStoreName","testStoreAddress", new HashSet<>(), company, new HashSet<>());
        store.setId(-2L);
        entityManager.persist(store);
        entityManager.flush();
        return store;
    }
    private StoreItem createStoreItem(Store store, CompanyItem companyItem){
        StoreItem storeItem = new StoreItem(store,companyItem,3500,10.0,5);
        storeItem.setId(-2L);
        entityManager.persist(storeItem);
        entityManager.flush();
        return storeItem;
    }
}
