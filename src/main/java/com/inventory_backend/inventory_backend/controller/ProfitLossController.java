package com.inventory_backend.inventory_backend.controller;

import com.inventory_backend.inventory_backend.dto.ProfitLossResponse;
import com.inventory_backend.inventory_backend.service.ProfitLossService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/profit-loss")
public class ProfitLossController {

    @Autowired
    private ProfitLossService service;


        @GetMapping("/{supplierId}")
        public ProfitLossResponse getSupplierProfitLoss(
                @PathVariable Long supplierId,
                @RequestParam LocalDate start,
                @RequestParam LocalDate end,
                @RequestParam(defaultValue = "CUSTOM") String periodType) {

            return service.calculateSupplierProfitLoss(supplierId, start, end, periodType);
        }

        @GetMapping("/overall")
        public ProfitLossResponse getOverAllProfitLoss(@RequestParam LocalDate start,
                                                        @RequestParam LocalDate end,
                                                       @RequestParam String periodType)
        {
               return service.calculateOverallProfitLoss(start,end,periodType);
        }
}
