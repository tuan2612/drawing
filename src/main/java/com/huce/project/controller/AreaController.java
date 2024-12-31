package com.huce.project.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.huce.project.dto.AreaDTO;
import com.huce.project.dto.ResponseAPIDTO;
import com.huce.project.entity.AreaEntity;
import com.huce.project.service.AreaService;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import java.lang.reflect.Type;
import lombok.RequiredArgsConstructor;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("api/v1/area")
@RequiredArgsConstructor
public class AreaController {
    private final AreaService areaService;
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<ResponseAPIDTO<List<AreaDTO>>> getAreas(@RequestParam(required = false) Integer id) {
        try {
            List<AreaEntity> areas;

            if (id != null) {
                // Fetch Area by parent ID
                Optional<AreaEntity> area = areaService.getAreaByParentId(id);
                if (area.isEmpty()) {
                    return ResponseEntity.ok(ResponseAPIDTO.<List<AreaDTO>>builder()
                            .code(HttpStatus.NOT_FOUND.value())
                            .message("Area not found")
                            .result(Collections.emptyList())
                            .build());
                }
                areas = List.of(area.get());
            } else {
                areas = areaService.getAllArea();
            }

            if (areas.isEmpty()) {
                return ResponseEntity.ok(ResponseAPIDTO.<List<AreaDTO>>builder()
                        .code(HttpStatus.OK.value())
                        .message("No areas found")
                        .result(Collections.emptyList())
                        .build());
            }

            // Map List<AreaEntity> to List<AreaDTO>
            Type listType = new TypeToken<List<AreaDTO>>() {}.getType();
            List<AreaDTO> mappedAreas = modelMapper.map(areas, listType);

            return ResponseEntity.ok(ResponseAPIDTO.<List<AreaDTO>>builder()
                    .code(HttpStatus.OK.value())
                    .message("Areas retrieved successfully")
                    .result(mappedAreas)
                    .build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ResponseAPIDTO.<List<AreaDTO>>builder()
                            .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("An error occurred: " + e.getMessage())
                            .result(Collections.emptyList())
                            .build());
        }
    }
}
