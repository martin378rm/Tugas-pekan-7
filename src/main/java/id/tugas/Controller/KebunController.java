package id.tugas.Controller;

import id.tugas.Model.Kebun;
import id.tugas.Util.JasperReportGenerator;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.scheduler.Scheduled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;


import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.Optional;



@Path("kebun")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class KebunController {

    // file
    int i = 1;

    @Inject
    JasperReportGenerator jasperReportGenerator;

    @Inject
    Mailer mailer;


    private static final Logger LOGGER = LoggerFactory.getLogger(KebunController.class);

    @GET
    public List<Kebun> getListAll() {
        return Kebun.listAll();
    }

    @GET
    @Path("{id}")
    public List<Kebun> getById(@PathParam("id") String id) {

        if (Objects.nonNull(Kebun.findByIdOptional(id))){
            return Kebun.list("id",id);
        }
        return Kebun.listAll();
    }

    @POST
    @Transactional
    @Scheduled(cron = "0 0 15 ? * SAT")
    public void tambahData() {
        Kebun kebun = new Kebun();
        kebun.setKomoditas("tomat");
        kebun.setTotal(500);
        kebun.persist();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public List<Kebun> update(@PathParam("id") String id,Kebun body) {
        Optional<Kebun> optionalKebun = Kebun.find("id",id).firstResultOptional();

        Kebun oldKebun = optionalKebun.get();

        if (Objects.nonNull(body.komoditas)) {
            oldKebun.setKomoditas(body.getKomoditas());
        }
        if (Objects.nonNull(body.total)) {
            oldKebun.setTotal(body.getTotal());
        }
        oldKebun.preUpdate();
        oldKebun.persist();
        return Kebun.list("id",id);
    }
    
    @DELETE
    @Path("{id}")
    @Transactional
    public List<Kebun> delete(@PathParam("id") String id) {
        Kebun.deleteById(id);
        return Kebun.listAll();
    }


    @GET
    @Path("report")
    @Scheduled(cron = "0 0 16 ? * SAT")
    public void get() throws Exception {
        String fileName = "report" + "_" + i + ".pdf";
        String outputFileName = "reporting/result_reporting/" + fileName;
        String jasperReportPath = "reporting/jasper_report/kebun.jrxml";
        jasperReportGenerator.generatePdfReport(jasperReportPath, outputFileName);
        i++;
    }


    @GET
    @Path("email")
    @Scheduled(cron = "0 0 15 28 * ?")
    public void sendEmailWithAttachment() {
        LOGGER.info("terkirim");
        mailer.send(Mail.withText("implements222@gmail.com",
                        "Testing",
                        "report kebun"
                ).addAttachment("report_1.pdf",new File("reporting/result_reporting/report_1.pdf"),"text/plain")
        );

    }

}
