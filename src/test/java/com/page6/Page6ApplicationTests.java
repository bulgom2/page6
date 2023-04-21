package com.page6;

import com.page6.entity.TagMap;
import com.page6.repository.TagMapRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class Page6ApplicationTests {
	@Autowired private TagMapRepository tagMapRepository;

	@Test
	void contextLoads() {
		List<TagMap> tagMaps = tagMapRepository.findAllByBoardId(19L);
		for(int i = 0; i < tagMaps.size(); i++)
			System.out.println(i + "번째 = " + tagMaps.get(i).getTag().getName());
	}

}
