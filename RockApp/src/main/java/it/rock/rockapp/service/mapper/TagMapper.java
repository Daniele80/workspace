package it.rock.rockapp.service.mapper;

import it.rock.rockapp.domain.Tag;
import it.rock.rockapp.service.dto.TagDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tag} and its DTO {@link TagDTO}.
 */
@Mapper(componentModel = "spring")
public interface TagMapper extends EntityMapper<TagDTO, Tag> {}
