package com.skynet.javafx.service;

import java.util.List;
import com.skynet.javafx.model.SimpleEntity;

public interface FrameService {

	List<? extends SimpleEntity> getData();
	
	void delete(Long id);
	
}
