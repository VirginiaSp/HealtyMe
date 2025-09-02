import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExaminationRecordMapper extends EntityMapper<ExaminationRecordDTO, ExaminationRecord> {
    @Override
    @Mapping(target = "examDate", source = "examDate")
    ExaminationRecord toEntity(ExaminationRecordDTO dto);

    @Override
    @Mapping(target = "examDate", source = "examDate")
    ExaminationRecordDTO toDto(ExaminationRecord entity);
}
