package com.vatek.hrmtool.service.serviceImpl;

import com.vatek.hrmtool.dto.ProjectDto.*;
import com.vatek.hrmtool.entity.ProjectOld;
import com.vatek.hrmtool.entity.UserOld;
import com.vatek.hrmtool.respository.old.ProjectOldRepository;
import com.vatek.hrmtool.respository.old.UserOldRepository;
import com.vatek.hrmtool.service.ProjectService;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private UserOldRepository userRepository;
    @Autowired
    private ProjectOldRepository projectRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CreateProjectDto createProject(CreateProjectDto dto) {
        if (dto.getProjectManager() == null || dto.getProjectManager().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project manager is required");
        }

        ProjectOld project = new ProjectOld();
        project.setProjectName(dto.getProjectName());
        project.setClientName(dto.getClientName());
        project.setStartDate(dto.getStartDate() != null ? dto.getStartDate().atStartOfDay() : null);
        project.setEndDate(dto.getEndDate());
        project.setState("INCOMING");
        project.setIsDeleted(false);

        // Set project manager
        UserOld projectManager = userRepository.findByIdAndIsDeletedFalse(dto.getProjectManager())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Project manager not found"));
        project.setProjectManager(projectManager);

        // Set members
        List<UserOld> members = new ArrayList<>();
        if (dto.getMembers() != null && !dto.getMembers().isEmpty()) {
            for (String memberId : dto.getMembers()) {
                UserOld member = userRepository.findByIdAndIsDeletedFalse(memberId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member not found: " + memberId));
                members.add(member);
            }
        }
        project.setMembers(members);

        ProjectOld savedProject = projectRepository.save(project);
        return mapToCreateProjectDto(savedProject);
    }

    @Override
    public UpdateProjectDto updateProject(UpdateProjectDto dto, String id) {
        ProjectOld project = projectRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        if (dto.getProjectName() != null) {
            project.setProjectName(dto.getProjectName());
        }
        if (dto.getClientName() != null) {
            project.setClientName(dto.getClientName());
        }
        if (dto.getStartDate() != null) {
            project.setStartDate(LocalDateTime.from(dto.getStartDate().atStartOfDay()));
        }
        if (dto.getEndDate() != null) {
            project.setEndDate(dto.getEndDate());
        }
        if (dto.getState() != null) {
            project.setState(dto.getState());
        }

        ProjectOld updatedProject = projectRepository.save(project);
        return modelMapper.map(updatedProject, UpdateProjectDto.class);
    }

    @Override
    public void addMember(String projectId, ProjectEmployeeDto dto) {
        ProjectOld project = projectRepository.findByIdAndIsDeletedFalse(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        if (dto.getMembers() != null && !dto.getMembers().isEmpty()) {
            List<UserOld> currentMembers = project.getMembers();
            if (currentMembers == null) {
                currentMembers = new ArrayList<>();
            }

            for (String memberId : dto.getMembers()) {
                UserOld member = userRepository.findByIdAndIsDeletedFalse(memberId)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Member not found: " + memberId));

                // Check if member already exists
                if (currentMembers.stream().noneMatch(m -> m.getId().equals(memberId))) {
                    currentMembers.add(member);
                }
            }

            project.setMembers(currentMembers);
            projectRepository.save(project);
        }
    }

    @Override
    public void deleteMember(String projectId, ProjectEmployeeDto dto) {
        ProjectOld project = projectRepository.findByIdAndIsDeletedFalse(String.valueOf(projectId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        if (dto.getMembers() != null && !dto.getMembers().isEmpty()) {
            List<UserOld> currentMembers = project.getMembers();
            if (currentMembers == null) {
                currentMembers = new ArrayList<>();
            }

            for (String memberId : dto.getMembers()) {
                boolean removed = currentMembers.removeIf(m -> m.getId().equals(memberId));
                if (!removed) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot find member " + memberId);
                }
            }

            project.setMembers(currentMembers);
            projectRepository.save(project);
        }
    }

    @Override
    public Page<GetListProjectDto> getAllProjects(String projectName, int offset, int limit) {
        Pageable page = PageRequest.of(offset, limit, Sort.by("id").descending());

        Page<ProjectOld> projects;
        if (projectName != null && !projectName.isEmpty()) {
            projects = projectRepository.findByProjectNameContainingAndIsDeletedFalse(projectName, page);
        } else {
            projects = projectRepository.findAllByIsDeletedFalse(page);
        }
        return projects.map(this::mapToGetListProjectDto);
    }

    @Override
    public ProjectDetailDto getProjectDetail(String id) {
        ProjectOld project = projectRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        return mapToProjectDetailDto(project);
    }

    @Override
    public void deleteProject(String id) {
        ProjectOld project = projectRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        project.setIsDeleted(true);
        projectRepository.save(project);
    }

    public ProjectPaginationResponse getUserProjects(String userId) {
        List<ProjectOld> projects = projectRepository.findByUserAsMember(userId);
        long total = projectRepository.countByIsDeletedFalse();

        List<GetListProjectDto> data = projects.stream()
                .map(this::mapToGetListProjectDto)
                .collect(Collectors.toList());

        return new ProjectPaginationResponse(data, total, data.size());
    }

    public ProjectPaginationResponse getPmProjects(String userId) {
        List<ProjectOld> projects = projectRepository.findByUserAsManagerOrMember(userId);
        long total = projectRepository.countByIsDeletedFalse();

        List<GetListProjectDto> data = projects.stream()
                .map(this::mapToGetListProjectDto)
                .collect(Collectors.toList());

        return new ProjectPaginationResponse(data, total, data.size());
    }

    private CreateProjectDto mapToCreateProjectDto(ProjectOld project) {
        CreateProjectDto dto = new CreateProjectDto();
        dto.setProjectName(project.getProjectName());
        dto.setClientName(project.getClientName());
        if (project.getStartDate() != null) {
            dto.setStartDate(project.getStartDate().toLocalDate());
        }
        dto.setEndDate(project.getEndDate());
        if (project.getProjectManager() != null) {
            dto.setProjectManager(project.getProjectManager().getId());
        }
        if (project.getMembers() != null) {
            dto.setMembers(project.getMembers().stream()
                    .map(UserOld::getId)
                    .collect(Collectors.toList()));
        }
        return dto;
    }

    private GetListProjectDto mapToGetListProjectDto(ProjectOld project) {
        GetListProjectDto dto = new GetListProjectDto();
        dto.setId(project.getId());
        dto.setProjectName(project.getProjectName());
        dto.setClientName(project.getClientName());
        dto.setState(project.getState());
        if (project.getStartDate() != null) {
            dto.setStartDate(project.getStartDate().toLocalDate());
        }
        dto.setEndDate(project.getEndDate());

        if (project.getProjectManager() != null) {
            dto.setProjectManager(mapToUserProjectDto(project.getProjectManager()));
        }

        if (project.getMembers() != null) {
            dto.setMembers(project.getMembers().stream()
                    .map(this::mapToUserProjectDto)
                    .collect(Collectors.toList()));
        } else {
            dto.setMembers(new ArrayList<>());
        }

        return dto;
    }

    private ProjectDetailDto mapToProjectDetailDto(ProjectOld project) {
        ProjectDetailDto dto = new ProjectDetailDto();
        dto.setId(project.getId());
        dto.setProjectName(project.getProjectName());
        dto.setClientName(project.getClientName());
        dto.setState(project.getState());
        if (project.getStartDate() != null) {
            dto.setStartDate(project.getStartDate().toLocalDate());
        }
        dto.setEndDate(project.getEndDate());

        if (project.getProjectManager() != null) {
            dto.setProjectManager(mapToUserDetailDto(project.getProjectManager()));
        }

        if (project.getMembers() != null) {
            dto.setMembers(project.getMembers().stream()
                    .map(this::mapToUserDetailDto)
                    .collect(Collectors.toList()));
        } else {
            dto.setMembers(new ArrayList<>());
        }

        return dto;
    }

    private GetListProjectDto.UserProjectDto mapToUserProjectDto(UserOld user) {
        GetListProjectDto.UserProjectDto dto = new GetListProjectDto.UserProjectDto();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        if (user.getAvatar() != null) {
            GetListProjectDto.ImageDto imageDto = new GetListProjectDto.ImageDto();
            imageDto.setId(user.getAvatar().getId());
            imageDto.setSrc(user.getAvatar().getSrc());
            dto.setAvatar(imageDto);
        }
        return dto;
    }

    private ProjectDetailDto.UserProjectDto mapToUserDetailDto(UserOld user) {
        ProjectDetailDto.UserProjectDto dto = new ProjectDetailDto.UserProjectDto();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        if (user.getAvatar() != null) {
            ProjectDetailDto.ImageDto imageDto = new ProjectDetailDto.ImageDto();
            imageDto.setId(user.getAvatar().getId());
            imageDto.setSrc(user.getAvatar().getSrc());
            dto.setAvatar(imageDto);
        }
        return dto;
    }
}
