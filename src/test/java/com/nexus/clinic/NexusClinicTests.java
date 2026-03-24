package com.nexus.clinic;

import com.nexus.clinic.dto.AppointmentForm;
import com.nexus.clinic.entity.*;
import com.nexus.clinic.repository.*;
import com.nexus.clinic.service.AppointmentService;
import com.nexus.clinic.service.EmailService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// ── Unit Tests ───────────────────────────────────────────────────

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock private AppointmentRepository appointmentRepository;
    @Mock private ServiceRepository     serviceRepository;
    @Mock private TherapistRepository   therapistRepository;
    @Mock private BranchRepository      branchRepository;
    @Mock private EmailService          emailService;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    @DisplayName("bookAppointment saves with PENDING status")
    void bookAppointment_savesWithPendingStatus() {
        AppointmentForm form = AppointmentForm.builder()
                .patientName("Ahmed Mohamed")
                .phone("01012345678")
                .email("ahmed@test.com")
                .serviceId(null)
                .preferredDate(LocalDate.now().plusDays(3))
                .preferredTime("10:00 AM")
                .build();

        Appointment saved = Appointment.builder()
                .id(1L)
                .patientName("Ahmed Mohamed")
                .status(AppointmentStatus.PENDING)
                .build();

        when(appointmentRepository.save(any(Appointment.class))).thenReturn(saved);

        Appointment result = appointmentService.bookAppointment(form);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getStatus()).isEqualTo(AppointmentStatus.PENDING);
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    @DisplayName("bookAppointment links service when serviceId provided")
    void bookAppointment_linksService() {
        Service service = Service.builder().id(2L).nameEn("Sports Rehab").build();
        when(serviceRepository.findById(2L)).thenReturn(Optional.of(service));

        AppointmentForm form = AppointmentForm.builder()
                .patientName("Sara")
                .phone("01099999999")
                .serviceId(2L)
                .preferredDate(LocalDate.now().plusDays(1))
                .preferredTime("2:00 PM")
                .build();

        ArgumentCaptor<Appointment> captor = ArgumentCaptor.forClass(Appointment.class);
        when(appointmentRepository.save(captor.capture()))
                .thenAnswer(i -> i.getArgument(0));

        appointmentService.bookAppointment(form);

        assertThat(captor.getValue().getService()).isNotNull();
        assertThat(captor.getValue().getService().getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("updateStatus changes appointment status correctly")
    void updateStatus_changesStatus() {
        Appointment apt = Appointment.builder()
                .id(5L).status(AppointmentStatus.PENDING).build();
        when(appointmentRepository.findById(5L)).thenReturn(Optional.of(apt));
        when(appointmentRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        appointmentService.updateStatus(5L, AppointmentStatus.CONFIRMED);

        assertThat(apt.getStatus()).isEqualTo(AppointmentStatus.CONFIRMED);
    }
}

// ── Validation Unit Tests ────────────────────────────────────────

class AppointmentFormValidationTest {

    private static jakarta.validation.Validator validator;

    @BeforeAll
    static void setup() {
        validator = jakarta.validation.Validation
                .buildDefaultValidatorFactory().getValidator();
    }

    @Test
    @DisplayName("Valid form has no violations")
    void validForm_noViolations() {
        AppointmentForm form = AppointmentForm.builder()
                .patientName("Ahmed Mohamed")
                .phone("01012345678")
                .email("ahmed@example.com")
                .serviceId(1L)
                .preferredDate(LocalDate.now().plusDays(1))
                .preferredTime("10:00 AM")
                .build();

        assertThat(validator.validate(form)).isEmpty();
    }

    @Test
    @DisplayName("Blank patient name triggers violation")
    void blankName_triggersViolation() {
        AppointmentForm form = AppointmentForm.builder()
                .patientName("").phone("01012345678")
                .serviceId(1L)
                .preferredDate(LocalDate.now().plusDays(1))
                .preferredTime("10:00 AM").build();

        var v = validator.validate(form);
        assertThat(v).anyMatch(x -> x.getPropertyPath().toString().equals("patientName"));
    }

    @Test
    @DisplayName("Past date triggers violation")
    void pastDate_triggersViolation() {
        AppointmentForm form = AppointmentForm.builder()
                .patientName("Test").phone("01012345678")
                .serviceId(1L)
                .preferredDate(LocalDate.now().minusDays(1))
                .preferredTime("10:00 AM").build();

        var v = validator.validate(form);
        assertThat(v).anyMatch(x -> x.getPropertyPath().toString().equals("preferredDate"));
    }

    @Test
    @DisplayName("Invalid phone triggers violation")
    void invalidPhone_triggersViolation() {
        AppointmentForm form = AppointmentForm.builder()
                .patientName("Test").phone("not-a-phone!")
                .serviceId(1L)
                .preferredDate(LocalDate.now().plusDays(1))
                .preferredTime("10:00 AM").build();

        var v = validator.validate(form);
        assertThat(v).anyMatch(x -> x.getPropertyPath().toString().equals("phone"));
    }
}

// ── Integration Tests ────────────────────────────────────────────

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MySQL;DB_CLOSE_DELAY=-1",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.flyway.enabled=false",
    "spring.mail.host=localhost",
    "spring.mail.port=3025",
    "app.clinic.email=test@test.com",
    "app.clinic.phone=00000000000",
    "app.clinic.whatsapp=00000000000",
    "app.admin.default-password=testpass123",
    "app.whatsapp.mode=link"
})
class PublicPageIntegrationTest {

    @Autowired MockMvc mockMvc;
    @MockBean  JavaMailSender mailSender;

    @Test @DisplayName("GET / returns 200")
    void homepageLoads() throws Exception {
        mockMvc.perform(get("/")).andExpect(status().isOk());
    }

    @Test @DisplayName("GET /about returns 200")
    void aboutLoads() throws Exception {
        mockMvc.perform(get("/about")).andExpect(status().isOk());
    }

    @Test @DisplayName("GET /services returns 200")
    void servicesLoads() throws Exception {
        mockMvc.perform(get("/services")).andExpect(status().isOk());
    }

    @Test @DisplayName("GET /book returns 200")
    void bookLoads() throws Exception {
        mockMvc.perform(get("/book")).andExpect(status().isOk());
    }

    @Test @DisplayName("GET /contact returns 200")
    void contactLoads() throws Exception {
        mockMvc.perform(get("/contact")).andExpect(status().isOk());
    }

    @Test @DisplayName("GET /faq returns 200")
    void faqLoads() throws Exception {
        mockMvc.perform(get("/faq")).andExpect(status().isOk());
    }

    @Test @DisplayName("GET /pricing returns 200")
    void pricingLoads() throws Exception {
        mockMvc.perform(get("/pricing")).andExpect(status().isOk());
    }

    @Test @DisplayName("POST /book valid form redirects to success")
    void postBooking_redirects() throws Exception {
        mockMvc.perform(post("/book")
               .with(csrf())
               .param("patientName", "Test Patient")
               .param("phone", "01012345678")
               .param("serviceId", "")
               .param("preferredDate", LocalDate.now().plusDays(3).toString())
               .param("preferredTime", "10:00 AM"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl("/book/success"));
    }

    @Test @DisplayName("POST /book missing phone returns form with errors")
    void postBooking_missingPhone_returnsErrors() throws Exception {
        mockMvc.perform(post("/book")
               .with(csrf())
               .param("patientName", "Test Patient")
               .param("serviceId", "1")
               .param("preferredDate", LocalDate.now().plusDays(3).toString())
               .param("preferredTime", "10:00 AM"))
               .andExpect(status().isOk())
               .andExpect(model().attributeHasFieldErrors("appointmentForm", "phone"));
    }

    @Test @DisplayName("GET /admin unauthenticated redirects to login")
    void adminRedirectsToLogin() throws Exception {
        mockMvc.perform(get("/admin"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test @DisplayName("GET /admin authenticated returns 200")
    @WithMockUser(roles = "ADMIN")
    void adminAccessibleWhenAuthenticated() throws Exception {
        mockMvc.perform(get("/admin")).andExpect(status().isOk());
    }

    @Test @DisplayName("GET /api/v1/health returns UP")
    void healthReturnsUp() throws Exception {
        mockMvc.perform(get("/api/v1/health"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.status").value("UP"));
    }

    @Test @DisplayName("GET /robots.txt contains disallow admin")
    void robotsTxt() throws Exception {
        mockMvc.perform(get("/robots.txt"))
               .andExpect(status().isOk())
               .andExpect(content().string(
                   org.hamcrest.Matchers.containsString("Disallow: /admin/")));
    }
}
