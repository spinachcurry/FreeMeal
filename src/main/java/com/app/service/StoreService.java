package com.app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.mapper.StoreMapper;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class StoreService {

	@Autowired
	private StoreMapper storeMapper;
	
	
}