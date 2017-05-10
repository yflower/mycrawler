package com.jal.crawler.web.service.impl;

import com.jal.crawler.web.data.enums.StatusEnum;
import com.jal.crawler.web.data.model.component.ComponentRelation;
import com.jal.crawler.web.service.IComponentSelectService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Created by jianganlan on 2017/4/30.
 */
@Service
public class ComponentSelectServiceImpl implements IComponentSelectService {

    private static final Logger LOGGER = Logger.getLogger(ComponentSelectServiceImpl.class.getSimpleName());

    private ThreadLocalRandom threadLocalRandom;

    @Override
    public Optional<ComponentRelation> selectComponent(List<ComponentRelation> componentRelations) {
        List<ComponentRelation> ableComponents = componentRelations.stream()
                .filter(t -> ableStatus(t.getStatus())).collect(Collectors.toList());
        threadLocalRandom = ThreadLocalRandom.current();

        if (ableComponents.isEmpty()) {
//            List<ComponentRelation> notInitComponents = componentRelations.stream()
//                    .filter(t -> t.getStatus() == StatusEnum.NO_INIT).collect(Collectors.toList());
            if (ableComponents.isEmpty()) {
                return Optional.empty();
            }

        }

        return Optional.of(ableComponents.get(Math.abs(threadLocalRandom.nextInt() % ableComponents.size())));

    }

    @Override
    public Optional<ComponentRelation> selectComponent(List<ComponentRelation> componentRelations, String taskTag) {
        List<ComponentRelation> ableComponents = componentRelations.stream()
                .filter(t -> ableStatus(t.getStatus())).collect(Collectors.toList());
        threadLocalRandom = ThreadLocalRandom.current();

        if (ableComponents.isEmpty()) {
//            List<ComponentRelation> notInitComponents = componentRelations.stream()
//                    .filter(t -> t.getStatus() == StatusEnum.NO_INIT).collect(Collectors.toList());
            if (ableComponents.isEmpty()) {
                return Optional.empty();
            }

        }

//        return Optional.of(ableComponents.get(Math.abs(threadLocalRandom.nextInt() % ableComponents.size())));

        return Optional.of(ableComponents.get(Math.abs(taskTag.hashCode() % ableComponents.size())));

    }


    private boolean ableStatus(StatusEnum statusEnum) {
        return statusEnum == StatusEnum.INIT || statusEnum == StatusEnum.STARTED;
    }


}
