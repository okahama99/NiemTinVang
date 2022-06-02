package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.entities.Report;
import com.ntv.ntvcons_backend.entities.ReportModels.ReportModel;
import com.ntv.ntvcons_backend.services.report.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/Report")
public class ReportController {
    @Autowired
    ReportService reportService;

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/createReport", produces = "application/json;charset=UTF-8")
    public HttpStatus createReport(@RequestBody int projectId,
                                   @RequestBody int reporterId,
                                   @RequestBody Timestamp reportDate,
                                   @RequestBody String reportDesc){

        Report result = reportService.createReport(projectId, reporterId, reportDate, reportDesc);
        if(result!=null){
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/updateReport", produces = "application/json;charset=UTF-8")
    public HttpStatus updateReport(@RequestBody ReportModel reportModel){
        boolean result = reportService.updateReport(reportModel);
        if(result){
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/getAll", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<Report> getAll() {
        List<Report> reports = reportService.getAll();
        return reports;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/deleteReport/{reportId}", produces = "application/json;charset=UTF-8")
    public HttpStatus deleteReport(@PathVariable(name = "reportId") int reportId){
        if(reportService.deleteReport(reportId))
        {
            return HttpStatus.OK;
        }else{
            return HttpStatus.BAD_REQUEST;
        }
    }
}
