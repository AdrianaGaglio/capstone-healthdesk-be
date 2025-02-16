//package epicode.it.healthdesk.entities.doctor;
//
//import epicode.it.healthdesk.entities.calendar.CalendarSvc;
//import epicode.it.healthdesk.entities.calendar.opening_day.OpeningDay;
//import epicode.it.healthdesk.entities.calendar.opening_day.OpeningDaySvc;
//import epicode.it.healthdesk.entities.calendar.opening_day.dto.OpeningDayUpdateRequest;
//import epicode.it.healthdesk.entities.doctor.dto.DoctorUpdateAddInfoRequest;
//import epicode.it.healthdesk.entities.doctor.dto.DoctorUpdateRequest;
//import epicode.it.healthdesk.entities.service.dto.DoctorServiceRequest;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.BeanUtils;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.cglib.core.Local;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import java.time.DayOfWeek;
//import java.time.LocalTime;
//import java.util.ArrayList;
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//@Order(2)
//public class DoctorSettings implements ApplicationRunner {
//    private final DoctorSvc doctorSvc;
//    private final CalendarSvc calendarSvc;
//    private final OpeningDaySvc openingDaySvc;
//
//    @Transactional
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//
//        Doctor d = doctorSvc.getByEmail("infohealthdesk@gmail.com");
//        List<OpeningDay> allDays = openingDaySvc.getByCalendar(d.getCalendar());
//        List<OpeningDayUpdateRequest> daysRequest = new ArrayList<>();
//
//        if (d.getServices().size() > 0) return;
//
//        allDays.forEach(day -> {
//            OpeningDayUpdateRequest r = new OpeningDayUpdateRequest();
//            BeanUtils.copyProperties(day, r);
//
//            r.setDayName(day.getDayName().toString());
//
//            if (day.getDayName().equals(DayOfWeek.TUESDAY)) {
//                r.setStartTime(LocalTime.of(16, 0));
//                r.setEndTime(LocalTime.of(20, 0));
//                r.setIsActive(true);
//            }
//
//            if (day.getDayName().equals(DayOfWeek.THURSDAY)) {
//                r.setStartTime(LocalTime.of(16, 0));
//                r.setEndTime(LocalTime.of(20, 0));
//                r.setIsActive(true);
//            }
//
//            daysRequest.add(r);
//        });
//
//        calendarSvc.updateDay(d.getCalendar().getId(), daysRequest);
//
//        DoctorUpdateAddInfoRequest request = new DoctorUpdateAddInfoRequest();
//
//        List<DoctorServiceRequest> services = new ArrayList<>();
//
//        DoctorServiceRequest s1 = new DoctorServiceRequest();
//        s1.setName("Consulenza");
//        s1.setDescription("Consulto volto ad individuare l'eventuale presenza di un problema psicologico/psichiatrico e le sue possibilità di gestione.");
//        s1.setOnline(true);
//        s1.setPrice(50);
//
//        DoctorServiceRequest s2 = new DoctorServiceRequest();
//        s2.setName("Psicoterapia individuale");
//        s2.setDescription("Finalizzato ad individuare e ad approfondire dinamiche ed elementi di vita disfunzionali e a modificarli.");
//        s2.setOnline(true);
//        s2.setPrice(70);
//
//        DoctorServiceRequest s3 = new DoctorServiceRequest();
//        s3.setName("Visita psichiatrica");
//        s3.setDescription("Quando vi è necessità di impostare una terapia farmacologica.");
//        s3.setOnline(true);
//        s3.setPrice(150);
//
//        DoctorServiceRequest s4 = new DoctorServiceRequest();
//        s4.setName("Visita a domicilio");
//        s4.setDescription("Riservata a chi si trova impossibilitato ad allontanarsi dal domicilio.");
//        s4.setOnline(false);
//        s4.setPrice(200);
//
//        DoctorServiceRequest s5 = new DoctorServiceRequest();
//        s5.setName("Visita di controllo");
//        s5.setDescription("Periodicamente è consigliabile rivalutare le condizioni di salute e la terapia farmacologica.");
//        s5.setOnline(true);
//        s5.setPrice(70);
//
//        services.addAll(List.of(s1, s2, s3, s4, s5));
//
//        request.setServices(services);
//        request.setId(d.getId());
//        doctorSvc.updateAddInfo(d.getId(), request);
//    }
//
//
//}
