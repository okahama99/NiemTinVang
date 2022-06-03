package com.ntv.ntvcons_backend.controllers;

import com.ntv.ntvcons_backend.entities.ReportDetail;
import com.ntv.ntvcons_backend.entities.ReportDetailModels.ReportDetailModel;
import com.ntv.ntvcons_backend.services.reportDetail.ReportDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/ReportDetail")
public class ReportDetailController {
    @Autowired
    ReportDetailService reportDetailService;

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping(value = "/createReportDetail", produces = "application/json;charset=UTF-8")
    public HttpStatus createReportDetail(){

        ReportDetail result = reportDetailService.createReportDetail();
        if(result!=null){
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/updateReport", produces = "application/json;charset=UTF-8")
    public HttpStatus updateReport(@RequestBody ReportDetailModel reportDetailModel){
        boolean result = reportDetailService.updateReport(reportDetailModel);
        if(result){
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping(value = "/getAll", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    List<ReportDetail> getAll() {
        List<ReportDetail> reports = reportDetailService.getAll();
        return reports;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/deleteReport/{reportDetailId}", produces = "application/json;charset=UTF-8")
    public HttpStatus deleteReport(@PathVariable(name = "reportDetailId") int reportDetailId){
        if(reportDetailService.deleteReport(reportDetailId))
        {
            return HttpStatus.OK;
        }else{
            return HttpStatus.BAD_REQUEST;
        }
    }
}
