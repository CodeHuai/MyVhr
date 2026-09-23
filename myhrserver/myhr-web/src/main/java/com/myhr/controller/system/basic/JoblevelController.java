package com.myhr.controller.system.basic;

import com.myhr.model.JobLevel;
import com.myhr.model.RespBean;
import com.myhr.service.JoblevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.List;

/**
 * 职称
 */
@RestController
@RequestMapping("/system/basic/joblevel")
public class JoblevelController implements Serializable {
    private static final long serialVersionUID = 1L;

    @Autowired
    private JoblevelService joblevelService;

    @GetMapping("/")
    public List<JobLevel> getAllPositions() {
        return joblevelService.selectJobLevels();
    }

    @PostMapping("/")
    public RespBean addPosition(@RequestBody JobLevel jobLevel) {
        if (joblevelService.insertJobLevel(jobLevel) == 1) {
            return RespBean.ok("操作成功！");
        }
        return RespBean.error("操作失败，请联系管理员！");
    }

    @PutMapping("/")
    public RespBean updatePositions(@RequestBody JobLevel jobLevel) {
        if (joblevelService.updateJobLevel(jobLevel) == 1) {
            return RespBean.ok("操作成功！");
        }
        return RespBean.error("操作失败，请联系管理员！");
    }

    @DeleteMapping("/{id}")
    public RespBean deleteByPrimaryKey(@PathVariable Long id) {
        Integer primaryKey = joblevelService.deleteJobLevels(id);
        if (primaryKey == 1) {
            return RespBean.ok("操作成功！");
        }
        return RespBean.error("操作失败，请联系管理员！");
    }

    @DeleteMapping("/")
    public RespBean deletePositionsByIds(@RequestParam(value = "ids") Long[] ids) {
        Integer inserted = joblevelService.deleteByIds(ids);
        if (inserted == ids.length) {
            return RespBean.ok("操作成功！");
        }
        return RespBean.error("操作失败，请联系管理员！");
    }
}
