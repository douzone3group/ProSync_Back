package com.douzone.prosync.task.service;

import com.douzone.prosync.common.PageInfo;
import com.douzone.prosync.exception.ApplicationException;
import com.douzone.prosync.exception.ErrorCode;
import com.douzone.prosync.status.TaskStatus;
import com.douzone.prosync.task.entity.Task;
import com.douzone.prosync.task.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    public Task createTask(Task task, Integer projectId, String userEmail) {
        if (task.getTaskStatus() == null) {
            task.setTaskStatus(TaskStatus.NO_STATUS);
        }
        return taskRepository.save(task, projectId);
    }

    public void updateTask(Task task, String userEmail) {

        Task findTask = findExistTask(task.getTaskId());
        Optional.ofNullable(task.getClassification())
                        .ifPresent(findTask::setClassification);
        Optional.ofNullable(task.getTitle())
                        .ifPresent(findTask::setTitle);
        Optional.ofNullable(task.getDetail())
                        .ifPresent(findTask::setDetail);
        Optional.ofNullable(task.getStartDate())
                        .ifPresent(findTask::setStartDate);
        Optional.ofNullable(task.getEndDate())
                        .ifPresent(findTask::setEndDate);
        Optional.ofNullable(task.getTaskStatus())
                        .ifPresent(findTask::setTaskStatus);

        taskRepository.updateTask(findTask);
    }

    public void deleteTask(Integer taskId, String userEmail) {
        verifyExistTask(taskId);

        //soft delete
        taskRepository.delete(taskId);
//        taskRepository.deleteById(taskId);
    }

    public Task findTask(Integer taskId, String userEmail) {
        return findExistTask(taskId);
    }

    public Task findExistTask(Integer taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new ApplicationException(ErrorCode.TASK_NOT_FOUND));
    }

    /**
     * 업무 리스트 조회
     */
    public Map<String, Object> findTaskList(Integer projectId, int offset, int size, String search, String userEmail) {
        List<Task> tasks = taskRepository.findAllByProject(projectId, offset * size, size, search);
        int totalElement = taskRepository.findTaskCount(projectId, search);
        PageInfo pageInfo = new PageInfo(offset+1, size, totalElement, (int)Math.ceil((double)totalElement / size));
        HashMap<String, Object> result = new HashMap<>();
        result.put("tasks", tasks);
        result.put("pageInfo", pageInfo);
        return result;
    }

    /**
     * 삭제된 task인 경우 예외 처리
     */
    private void verifyExistTask(Integer taskId) {
        if (taskRepository.isDeletedTask(taskId)) {
            throw new ApplicationException(ErrorCode.TASK_NOT_FOUND);
        }
    }
}
