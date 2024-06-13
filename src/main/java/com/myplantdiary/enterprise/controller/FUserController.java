package com.myplantdiary.enterprise.controller;

import com.myplantdiary.enterprise.entity.TrainMaster;
import com.myplantdiary.enterprise.entity.User;
import com.myplantdiary.enterprise.entity.OTP;
import com.myplantdiary.enterprise.entity.Messages;
import com.myplantdiary.enterprise.entity.UserData;
import com.myplantdiary.enterprise.service.*;
import com.myplantdiary.enterprise.repository.OTPRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

@Controller
public class FUserController {

    private final UserServiceImpl userServiceImpl;
    private final OTPService Otp;
    private final OTPRepository OtpRepository;
    private final TrainService trainService;
    private final MessagesService messagesService;

    @Autowired
    public FUserController(UserServiceImpl userServiceImpl, OTPService Otp, OTPRepository OtpRepository, TrainService trainService, MessagesService messagesService) {
        this.userServiceImpl = userServiceImpl;
        this.Otp = Otp;
        this.OtpRepository = OtpRepository;
        this.trainService = trainService;
        this.messagesService = messagesService;
    }

    private static String generateBookingId() {
        String currentYear = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy"));
        String alphabets = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder randomAlphabets = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 4; i++) {
            randomAlphabets.append(alphabets.charAt(random.nextInt(alphabets.length())));
        }

        // Generate eight random digits
        int randomDigits = 10000000 + random.nextInt(90000000);

        // Combine current year, alphabets, and digits
        return currentYear + randomAlphabets.toString() + randomDigits;
    }

    @RequestMapping("/sot")
    public String index(HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        return "start";
    }

    @PostMapping("/home")
    public String seatBook(@RequestParam("departure") String departure,
                           @RequestParam("destination") String destination,
                           @RequestParam("departureDate1") LocalDate departureDate1,
                           @RequestParam(name = "departureDate2", required = false) LocalDate departureDate2,
                           @RequestParam("numberOfTickets") long numberOfTickets,
                           @RequestParam(name = "roundTrip", required = false) boolean roundTrip,
                           Model model, HttpSession session,
                           RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        redirectAttributes.addFlashAttribute("departure",departure);
        redirectAttributes.addFlashAttribute("destination",destination);
        redirectAttributes.addFlashAttribute("departureDate1",departureDate1);
        if (departureDate2 != null) {
            redirectAttributes.addFlashAttribute("departureDate2", departureDate2);
        }
        redirectAttributes.addFlashAttribute("numberOfTickets",numberOfTickets);
        if (roundTrip) {
            redirectAttributes.addFlashAttribute("roundTrip", true);
        }
        else {
            redirectAttributes.addFlashAttribute("roundTrip", false);
        }
        return "redirect:/submitUserData";
    }

    @RequestMapping("/")
    public String loginOption(HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user != null) {
            if (user.getRole().equalsIgnoreCase("admin"))
            {
                // SET ERROR MESSAGE WHILE REDIRECTING TO HOMEPAGE
                return "redirect:/admin/home";
            }
            else
                return "redirect:/home";
        }
        return "loginOption";
    }

    @RequestMapping("/home")
    public String seatBooking(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        return "seat_booking";
    }
    @RequestMapping("/admin/query")
    public String query(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        else if (!user.getRole().equalsIgnoreCase("admin"))
        {
            // SET ERROR MESSAGE WHILE REDIRECTING TO HOMEPAGE
            return "redirect:/home";
        }
        List<Messages> messages = messagesService.getAllMessages();
        model.addAttribute("messages", messages);
        int count = messages.size();
        model.addAttribute("count", count);
        return "admin/queries";
    }
    @RequestMapping("/admin/addstations")
    public String addstations(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        else if (!user.getRole().equalsIgnoreCase("admin"))
        {
            // SET ERROR MESSAGE WHILE REDIRECTING TO HOMEPAGE
            return "redirect:/home";
        }
        List<Messages> messages = messagesService.getAllMessages();
        int count = messages.size();
        model.addAttribute("count", count);
        return "admin/addstations";
    }

    @RequestMapping("/my_tickets")
    public String mytickets(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        List<UserData> tickets = userServiceImpl.getUserDataByEmail(user.getEmail());
        List<UserData> bookedTicket = new ArrayList<>();
        for(UserData ticket:tickets)
        {
            if(ticket.isPayment_done())
            {
                bookedTicket.add(ticket);
            }
        }
        model.addAttribute("bookedTicket",bookedTicket);
        model.addAttribute("user",user);
        return "myticket";
    }

    @RequestMapping("/contact")
    public String contactUs(HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        return "contact";
    }
    @PostMapping("/contact")
    public String contactUs(@RequestParam("email") String email,
                            @RequestParam("messages") String messages,
                            Model model,
                            HttpSession session){
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        Messages messages1 = new Messages();
        messages1.setMessages(messages);
        messages1.setEmail(email);
        messagesService.save(messages1);
        model.addAttribute("test","Query saved successfully!!");
        return "contact";
    }

    @RequestMapping("/admin_report")
    public String showEntities(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        else if (!user.getRole().equalsIgnoreCase("admin"))
        {
            // SET ERROR MESSAGE WHILE REDIRECTING TO HOMEPAGE
            return "redirect:/home";
        }
        List<User> entities = userServiceImpl.getAllEntities();
        model.addAttribute("entities", entities);
        return "admin_report"; // Thymeleaf template name
    }

    public static boolean sendEmail(String first_name, String last_name, String receiver, int OTP, LocalDateTime time) {

        final String username = "suman.dutta.accenture@gmail.com";
        final String password = "ybctusvimeypsnoa";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.addHeader("Content-type", "text/HTML; charset=UTF-8");
            message.addHeader("format", "flowed");
            message.addHeader("Content-Transfer-Encoding", "8bit");

            message.setFrom(new InternetAddress("suman.dutta.accenture@gmail.com","Pod1 OTP Notifier"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(receiver)
            );
            DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDateTime = time.format(customFormatter);

            message.setSubject("OTP for Email Registration");
            message.setContent(String.format("Dear %s %s,<br><br>The OTP for user login/registration is: <b>%08d</b><br>This OTP is only valid for 5 minutes from now (<b>%s</b>)<br><br>With warm regards,<br>Team Pod1", first_name, last_name, OTP, formattedDateTime), "text/html");
            System.out.println("For email - "+receiver+" || OTP - "+OTP);
            Transport.send(message);
            return true;

        }
        catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("/registration")
    public String getRegistrationPage(Model model){
        String s = model.containsAttribute("error_message")?"form-banner2":"form-banner";
        model.addAttribute("s", s);
        return "registration";
    }

    @PostMapping("/registration")
    public String putRegistrationPage(@RequestParam("first_name") String first_name,
                                      @RequestParam("last_name") String last_name,
                                      @RequestParam("email") String email,
                                      @RequestParam("password") String password,
                                      HttpSession session,
                                      Model model, RedirectAttributes redirectAttributes){
        if(Otp.isRegistered(email))
        {
            model.addAttribute("error_message", "User with email ID: "+email+" already exists\nPlease change the email ID, or, proceed to Login page!");
            model.addAttribute("s", "form-banner2");
            return "registration";
        }
        redirectAttributes.addFlashAttribute("first_name", first_name);
        redirectAttributes.addFlashAttribute("last_name", last_name);
        redirectAttributes.addFlashAttribute("email", email);
        redirectAttributes.addFlashAttribute("p1", password);
        Random random = new Random();
        int otp = 10000000 + random.nextInt(90000000);
        LocalDateTime time = LocalDateTime.now();
        Otp.saveOtp(email, time, otp, false);
        if(sendEmail(first_name, last_name, email, otp, time))
        {
            return "redirect:/validate";
        }
        model.addAttribute("error_message", "Cannot send email to this email ID: "+email+"\nPlease change the email ID, and try again!");
        model.addAttribute("s", "form-banner2");
        return "registration";
    }

    @PostMapping("/login")
    public String validateLogin(@RequestParam("email") String email,
                                @RequestParam("password") String password,
                                HttpSession session,
                                HttpServletRequest request,
                                Model model,
                                RedirectAttributes redirectAttributes) {
        User user = userServiceImpl.findByEmail(email);
        if (user != null && password.equals(user.getPassword())) {
            redirectAttributes.addFlashAttribute("email", email);
            Random random = new Random();
            int otp = 10000000 + random.nextInt(90000000);
            LocalDateTime time = LocalDateTime.now();
            Otp.saveOtp(email, time, otp, true);
            String first_name = user.getFirstName(), last_name = user.getLastName();
            if(sendEmail(first_name, last_name, email, otp, time))
            {
                HttpSession session1 = request.getSession(false);
                if (session1 != null) {
                    session1.invalidate();
                }
//                if (session != null) {
//                    session.invalidate();
//                }
                session1 = request.getSession(true);
                session1.setAttribute("email", email);
                session1.setAttribute("first_name", first_name);
                session1.setAttribute("last_name", last_name);

//                redirectAttributes.addFlashAttribute("first_name", first_name);
//                redirectAttributes.addFlashAttribute("last_name", last_name);
//                redirectAttributes.addFlashAttribute("email", email);
                return "redirect:/validatelogin";
            }
            redirectAttributes.addFlashAttribute("message", "Cannot send email to this email ID: "+email+"\nPlease wait or contact the admin!");
            return "redirect:/login";
//            session.setAttribute("user", user);
//            if(!user.getRole().equalsIgnoreCase("Admin")) {
//                session.setMaxInactiveInterval(300);
//            }
//            if (user.getRole().equalsIgnoreCase("admin"))
//            {
//                // SET ERROR MESSAGE WHILE REDIRECTING TO HOMEPAGE
//                return "redirect:/admin/home";
//            }
//            else
//                return "redirect:/home";
        }
        else {
            redirectAttributes.addFlashAttribute("message","Wrong Email ID or Password");
            return "redirect:/login";
        }
    }

    @PostMapping("/validatelogin")
    public String putOTPL(@RequestParam("OTP") String OTP, Model model, RedirectAttributes redirectAttributes,
                          HttpServletRequest request,
                          HttpSession session) {
        String email = session.getAttribute("email").toString();
        //String password = session.getAttribute("password").toString();
        //String first_name = session.getAttribute("first_name").toString();
        //String last_name = session.getAttribute("last_name").toString();
        OTP OO = Otp.getOTP(email);
        User user = userServiceImpl.findByEmail(email);
        if(OO==null)
        {
            redirectAttributes.addFlashAttribute("message", "Please register the email & generate OTP for this email ID: "+email+" first!");
            return "redirect:/login";
        }
        if(ChronoUnit.MINUTES.between(OO.getTime(), LocalDateTime.now())>5)
        {
            redirectAttributes.addFlashAttribute("message", "OTP for this email ID: "+email+" has expired!\nPlease regenerate the OTP and re-register the User!");
            return "redirect:/login";
        }
        int otp=-9474784;
        try {
            otp = Integer.parseInt(OTP);
        }
        catch (NumberFormatException e) {
//            redirectAttributes.addFlashAttribute("first_name", first_name);
//            redirectAttributes.addFlashAttribute("last_name", last_name);
//            redirectAttributes.addFlashAttribute("email", email);
            redirectAttributes.addFlashAttribute("error_message", "Wrong OTP!!\nPlease Enter the Valid OTP");
            redirectAttributes.addFlashAttribute("s", "form-banner2");
            return "redirect:/validatelogin";
        }
        if(otp!=OO.getOtp()) {
//            redirectAttributes.addFlashAttribute("first_name", first_name);
//            redirectAttributes.addFlashAttribute("last_name", last_name);
//            redirectAttributes.addFlashAttribute("email", email);
            redirectAttributes.addFlashAttribute("error_message", "Wrong OTP!!\nPlease Enter the Valid OTP");
            redirectAttributes.addFlashAttribute("s", "form-banner2");
            return "redirect:/validatelogin";
        }
        HttpSession session1 = request.getSession(false);
        if (session1 != null) {
            session1.invalidate();
        }
        session1 = request.getSession(true);
        session1.setAttribute("user", user);
//        session.setAttribute("user", user);
        return "redirect:/home";
    }

    @RequestMapping("/validatelogin")
    public String getOTPL(Model model, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpSession session){
        String s = model.containsAttribute("error_message")?"form-banner2":"form-banner";
        String email = session.getAttribute("email").toString();
        //String password = session.getAttribute("password").toString();
        String first_name = session.getAttribute("first_name").toString();
        String last_name = session.getAttribute("last_name").toString();
        model.addAttribute("s", s);
        model.addAttribute("email", email);
        model.addAttribute("first_name", first_name);
        model.addAttribute("last_name", last_name);
        return "loginOTP";
    }

    @RequestMapping("/validate")
    public String getOTP(Model model, RedirectAttributes redirectAttributes){
        String s = model.containsAttribute("error_message")?"form-banner2":"form-banner";
        model.addAttribute("s", s);
        return "registrationOTP";
    }

    @PostMapping("/validate")
    public String putOTP(@RequestParam("first_name") String first_name,
                         @RequestParam("last_name") String last_name,
                         @RequestParam("email") String email,
                         @RequestParam("p1") String p1,
                         @RequestParam("OTP") String OTP, Model model, RedirectAttributes redirectAttributes) {
        OTP OO = Otp.getOTP(email);
        if(OO==null)
        {
            redirectAttributes.addFlashAttribute("error_message", "Please register the email & generate OTP for this email ID: "+email+" first!");
            return "redirect:/registration";
        }
        if(OO.getRegister())
        {
            redirectAttributes.addFlashAttribute("error_message", "User with email ID: "+email+" already exists\nPlease change the email ID, or, proceed to Login page!");
            return "redirect:/registration";
        }
        if(ChronoUnit.MINUTES.between(OO.getTime(), LocalDateTime.now())>5)
        {
            redirectAttributes.addFlashAttribute("error_message", "OTP for this email ID: "+email+" has expired!\nPlease regenerate the OTP and re-register the User!");
            return "redirect:/registration";
        }
        int otp=-9474784;
        try {
            otp = Integer.parseInt(OTP);
        }
        catch (NumberFormatException e) {
            model.addAttribute("first_name", first_name);
            model.addAttribute("last_name", last_name);
            model.addAttribute("email", email);
            model.addAttribute("p1", p1);
            model.addAttribute("error_message", "Wrong OTP!!\nPlease Enter the Valid OTP");
            model.addAttribute("s", "form-banner2");
            return "registrationOTP";
        }
        if(otp!=OO.getOtp()) {
            model.addAttribute("first_name", first_name);
            model.addAttribute("last_name", last_name);
            model.addAttribute("email", email);
            model.addAttribute("p1", p1);
            model.addAttribute("error_message", "Wrong OTP!!\nPlease Enter the Valid OTP");
            model.addAttribute("s", "form-banner2");
            return "registrationOTP";
        }
        User user = new User(email, email, p1, "User", first_name, last_name);
        userServiceImpl.save(user);
        OO.setRegister(true);
        OtpRepository.save(OO);
        redirectAttributes.addFlashAttribute("message", "User with this email ID: "+email+" successfully registered, Please login!");
        return "redirect:/login";
    }

    @RequestMapping("/login")
    public String showLoginForm(Model model) {
        return "userLogin";
    }

    @RequestMapping("/loginAdmin")
    public String showAdminLoginForm() {
        return "adminLogin";
    }

    @PostMapping("/loginAdmin")
    public String validateAdminLogin(@RequestParam("email") String email,
                                @RequestParam("password") String password,
                                HttpSession session, Model model) {
        User user = userServiceImpl.findByEmail(email);
        if (user != null && password.equals(user.getPassword())) {
            session.setAttribute("user", user);
            if (!user.getRole().equalsIgnoreCase("admin"))
            {
                // SET ERROR MESSAGE WHILE REDIRECTING TO HOMEPAGE
                return "redirect:/home";
            }
            else
                return "redirect:/admin/home";
        }
        else {
            return "adminLogin";
        }
    }

    @RequestMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

//        @PostMapping("/submitUserData")
//        public String submitUserData(@ModelAttribute("userData") UserData userData, Model model) {
//            userServiceImpl.saveUserData(userData);
//            model.addAttribute("message", "Data submitted successfully");
//            return "confirmationPage"; // Redirect to a confirmation page
//        }

        @RequestMapping("/viewUserData")
        public String viewUserData(Model model, HttpSession session) {
            User user = (User) session.getAttribute("user");
            if (user == null) {
                return "redirect:/login";
            }
            List<UserData> userDataList = userServiceImpl.getAllUserData();
            model.addAttribute("userDataList", userDataList);
            return "viewUserDataPage"; // Thymeleaf template name for viewing user data
        }



    @RequestMapping("/admin/home")
    public String financial_report(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        else if (!user.getRole().equalsIgnoreCase("admin"))
        {
            // SET ERROR MESSAGE WHILE REDIRECTING TO HOMEPAGE
            return "redirect:/home";
        }
        List<Object[]> totalPassengersAndEarnings = userServiceImpl.getTotalPassengersAndEarnings();
        List<Object[]> destinationStats = userServiceImpl.getDestinationStats();
        List<Object[]> highestEarningStation = userServiceImpl.getHighestEarningStation();
        List<Messages> messages = messagesService.getAllMessages();
        int count = messages.size();
        model.addAttribute("count", count);
        model.addAttribute("destinationStats", destinationStats);
        model.addAttribute("highestEarningStation", highestEarningStation);
        model.addAttribute("totalPassengersAndEarnings", totalPassengersAndEarnings);
        return "admin/financialReport"; // Thymeleaf template name
    }

    @RequestMapping("/admin/bookings")
    public String booking_details(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        else if (!user.getRole().equalsIgnoreCase("admin"))
        {
            // SET ERROR MESSAGE WHILE REDIRECTING TO HOMEPAGE
            return "redirect:/home";
        }
        List<UserData> bookingdetails = userServiceImpl.getAllUserData();
        List<UserData> booked = new ArrayList<>();
        for(UserData ticket:bookingdetails)
        {
            if(ticket.isPayment_done())
            {
                booked.add(ticket);
            }
        }
        model.addAttribute("bookingdetails", booked);
        List<Messages> messages = messagesService.getAllMessages();
        int count = messages.size();
        model.addAttribute("count", count);
        return "admin/bookingdetails";
    }

    @RequestMapping("/admin/settings")
    public String settings(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        else if (!user.getRole().equalsIgnoreCase("admin"))
        {
            // SET ERROR MESSAGE WHILE REDIRECTING TO HOMEPAGE
            return "redirect:/home";
        }
        List<Messages> messages = messagesService.getAllMessages();
        int count = messages.size();
        model.addAttribute("count", count);
        return "admin/setting"; // Thymeleaf template name
    }

    @RequestMapping("/admin/profile")
    public String profile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        else if (!user.getRole().equalsIgnoreCase("admin"))
        {
            // SET ERROR MESSAGE WHILE REDIRECTING TO HOMEPAGE
            return "redirect:/home";
        }
        List<Messages> messages = messagesService.getAllMessages();
        int count = messages.size();
        model.addAttribute("count", count);
        return "admin/profile"; // Thymeleaf template name
    }

    @RequestMapping("/admin/passengers")
    public String passenger_details(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        else if (!user.getRole().equalsIgnoreCase("admin"))
        {
            // SET ERROR MESSAGE WHILE REDIRECTING TO HOMEPAGE
            return "redirect:/home";
        }
        List<User> entities = userServiceImpl.getAllEntities();
        model.addAttribute("entities", entities);
        List<Messages> messages = messagesService.getAllMessages();
        int count = messages.size();
        model.addAttribute("count", count);
        return "admin/passengerdetails"; // Thymeleaf template name
    }

    @RequestMapping("/admin/stations")
    public String station_details(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        else if (!user.getRole().equalsIgnoreCase("admin"))
        {
            // SET ERROR MESSAGE WHILE REDIRECTING TO HOMEPAGE
            return "redirect:/home";
        }
        List<TrainMaster> stations = userServiceImpl.getAllTrainData();
        List<Object[]> totalPassengersAndEarnings = userServiceImpl.getTotalPassengersAndEarnings();
//        List<UserData> uniqueUserData = userServiceImpl.getUniqueUserDataForDestination(destination);
        List<Object[]> destinationStats = userServiceImpl.getDestinationStats();
        List<Object[]> highestEarningStation = userServiceImpl.getHighestEarningStation();
//        model.addAttribute("destination", destination);
//        model.addAttribute("uniqueUserData", uniqueUserData);
        model.addAttribute("destinationStats", destinationStats);
        model.addAttribute("highestEarningStation", highestEarningStation);
        model.addAttribute("totalPassengersAndEarnings", totalPassengersAndEarnings);
        model.addAttribute("stations", stations);
        List<Messages> messages = messagesService.getAllMessages();
        int count = messages.size();
        model.addAttribute("count", count);
        return "admin/stationdetails";
    }

    @GetMapping("/submitUserData")
    public String submitUserData(Model model, HttpSession session,
                                 RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        UserData userData = new UserData();
        String bookingId = generateBookingId();
        String departure = (String)model.getAttribute("departure");
        String destination = (String)model.getAttribute("destination");
        userData.setBookingId(bookingId);
        userData.setDeparture(departure);
        userData.setDestination(destination);
        userData.setDepartureDate1((LocalDate)model.getAttribute("departureDate1"));
        if(model.getAttribute("departureDate2") != null) {
            userData.setDepartureDate2((LocalDate) model.getAttribute("departureDate2"));
        }
        userData.setNo_tickets((long)model.getAttribute("numberOfTickets"));
        userData.setReturnExist((boolean)model.getAttribute("roundTrip"));
        userData.setSubmission_time(LocalDateTime.now());
        userData.setEmail(user.getEmail());
        userServiceImpl.saveUserData(userData);
        List<TrainMaster> trains = trainService.findTrainsByStations(departure, destination);
        model.addAttribute("trains", trains);
        redirectAttributes.addFlashAttribute("trains", trains);
        redirectAttributes.addFlashAttribute("bookingId", bookingId);
        trains.forEach(train -> System.out.println("Train: " + train.getT_name() + " - " + train.getDeparture() + " to " + train.getDestination()));
        model.addAttribute("message", "Data submitted successfully");

        // Redirect to the "destinationSelected" page
        return "redirect:/destinationSelected";
//            return "confirmationPage"; // Redirect to a confirmation page
    }

    @RequestMapping("/destinationSelected")
    public String showDestinationSelected(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        // The selected destination is automatically available in the model due to flash attributes
        return "destinationSelected";
    }

    @RequestMapping("/bookingDetails")
    public String showBookingDetails(@RequestParam String bookingId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        UserData userData = userServiceImpl.findByBookingId(bookingId);
        model.addAttribute("userData",userData);
        return "bookingDetails";
    }
    @RequestMapping("/payment")
    public String showPaymentPage(@RequestParam String bookingId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("bookingId", bookingId);
        return "payment";
    }
    @RequestMapping("/paymentOption")
    public String showPaymentOptionsPage(@RequestParam String bookingId, @RequestParam Long tID, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("bookingId", bookingId);
        model.addAttribute("tID", tID);
        String url1 = "/cashpayment?bookingId=" + bookingId + "&tID=" + tID;
        String url2 = "/cardpayment?bookingId=" + bookingId + "&tID=" + tID;
        model.addAttribute("url1", url1);
        model.addAttribute("url2", url2);
        TrainMaster trainMaster = trainService.findbyTno(tID);
        UserData userData = userServiceImpl.findByBookingId(bookingId);
        model.addAttribute("trainMaster", trainMaster);
        model.addAttribute("userData", userData);
        model.addAttribute("firstname", user.getFirstName());
        model.addAttribute("lastname", user.getLastName());
        return "paymentOption";
    }
    private boolean isValidCreditCard(String creditCardNumber) {
        // Implement Luhn algorithm to check credit card validity
        // You can find various implementations online
        // Here is a simple example:
        int sum = 0;
        boolean alternate = false;
        for (int i = creditCardNumber.length() - 1; i >= 0; i--) {
            int digit = Integer.parseInt(creditCardNumber.substring(i, i + 1));
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
            alternate = !alternate;
        }
        return sum % 10 == 0;
    }
    @RequestMapping("/cardpayment")
    public String cardpayment(@RequestParam String bookingId, @RequestParam Long tID, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if(!model.containsAttribute("isValid")) {
            model.addAttribute("isValid", true);
        }
        model.addAttribute("bookingId", bookingId);
        model.addAttribute("tID", tID);
        TrainMaster trainMaster = trainService.findbyTno(tID);
        UserData userData = userServiceImpl.findByBookingId(bookingId);
        model.addAttribute("trainMaster", trainMaster);
        model.addAttribute("userData", userData);
        model.addAttribute("firstname", user.getFirstName());
        model.addAttribute("lastname", user.getLastName());
        return "cardpayment";
    }
    @PostMapping("/cardpayment")
    public String cardPayment(@RequestParam("creditCardNumber") String creditCardNumber, @RequestParam String bookingId, @RequestParam Long tID, RedirectAttributes redirectAttributes, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        if(isValidCreditCard(creditCardNumber)){
            redirectAttributes.addFlashAttribute("card", creditCardNumber.substring(12,16));
            return String.format("redirect:/confirmation?bookingId=%s&tID=%s", bookingId, tID);
        }
        redirectAttributes.addFlashAttribute("isValid", false);
        return String.format("redirect:/cardpayment?bookingId=%s&tID=%s", bookingId, tID);
    }
    @RequestMapping("/cashpayment")
    public String cashpayment(@RequestParam String bookingId, @RequestParam Long tID, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("bookingId", bookingId);
        model.addAttribute("tID", tID);
        TrainMaster trainMaster = trainService.findbyTno(tID);
        UserData userData = userServiceImpl.findByBookingId(bookingId);
        model.addAttribute("trainMaster", trainMaster);
        model.addAttribute("userData", userData);
        model.addAttribute("firstname", user.getFirstName());
        model.addAttribute("lastname", user.getLastName());
        return "cashpayment";
    }

    @PostMapping("/cashpayment")
    public String cashpayment(@RequestParam Long amount, @RequestParam String bookingId, @RequestParam Long tID, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("bookingId", bookingId);
        model.addAttribute("tID", tID);
        TrainMaster trainMaster = trainService.findbyTno(tID);
        UserData userData = userServiceImpl.findByBookingId(bookingId);
        long totalAmount = trainMaster.getFare()*userData.getNo_tickets();
        if (amount < totalAmount)
        {
            redirectAttributes.addFlashAttribute("error","Provided Amount was less than the total fare");
            return String.format("redirect:/cashpayment?bookingId=%s&tID=%s",bookingId,tID);
        }
        model.addAttribute("trainMaster", trainMaster);
        model.addAttribute("userData", userData);
        model.addAttribute("firstname", user.getFirstName());
        model.addAttribute("lastname", user.getLastName());
        redirectAttributes.addFlashAttribute("change",amount - totalAmount);
        return String.format("redirect:/confirmation?bookingId=%s&tID=%s", bookingId, tID);
    }

    @RequestMapping("/confirmation")
    public String confirmation(@RequestParam String bookingId, @RequestParam Long tID, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        model.addAttribute("name",user.getFirstName()+' '+user.getLastName());
        model.addAttribute("bookingId", bookingId);
        model.addAttribute("tID", tID);
        TrainMaster trainMaster = trainService.findbyTno(tID);
        UserData userData = userServiceImpl.findByBookingId(bookingId);
        userData.setPayment_done(true);
        userData.setPrice1(trainMaster.getFare());
        userData.setDepartureTime1(trainMaster.getTime());
        userData.setTname(trainMaster.getT_name()+" - "+trainMaster.getType());
        userServiceImpl.saveUserData(userData);
        model.addAttribute("trainMaster", trainMaster);
        model.addAttribute("userData", userData);
        model.addAttribute("firstname", user.getFirstName());
        model.addAttribute("lastname", user.getLastName());
        if (model.containsAttribute("change"))
        {
            userData.setPayment_method("cash");
            userServiceImpl.saveUserData(userData);
            return "confirmationPage";
        }
        userData.setPayment_method("credit_card");
        userServiceImpl.saveUserData(userData);
        return "confirmation";
    }


}


