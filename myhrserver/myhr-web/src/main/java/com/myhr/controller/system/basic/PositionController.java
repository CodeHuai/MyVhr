package com.myhr.controller.system.basic;

import com.myhr.mapper.PositionMapper;
import com.myhr.model.Position;
import com.myhr.model.RespBean;
import com.myhr.service.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/basic/pos")
public class PositionController {
    @Autowired
    private PositionService positionService;
    @Autowired
    private PositionMapper positionMapper;

    @GetMapping("/")
    public List<Position> getAllPositions() {
        return positionService.getAllPositions();
    }

    @PostMapping("/")
    public RespBean addPosition(@RequestBody Position position) {
        if (positionService.addPosition(position) == 1) {
            return RespBean.ok("操作成功！");
        }
        return RespBean.error("操作失败，请联系管理员！");
    }

    @PutMapping("/")
    public RespBean updatePositions(@RequestBody Position position) {
        if (positionService.updateByPrimaryKeySelective(position) == 1) {
            return RespBean.ok("操作成功！");
        }
        return RespBean.error("操作失败，请联系管理员！");
    }

    @DeleteMapping("/{id}")
    public RespBean deleteByPrimaryKey(@PathVariable Long id) {
        Integer primaryKey = positionService.deleteByPrimaryKey(id);
        if (primaryKey == 1) {
            return RespBean.ok("操作成功！");
        }
        return RespBean.error("操作失败，请联系管理员！");
    }

    @DeleteMapping("/")
    public RespBean deletePositionsByIds(@RequestParam(value = "ids") Long[] ids) {
        Integer inserted = positionService.deletePositionsByIds(ids);
        if (inserted == ids.length) {
            return RespBean.ok("操作成功！");
        }
        return RespBean.error("操作失败，请联系管理员！");
    }
}
