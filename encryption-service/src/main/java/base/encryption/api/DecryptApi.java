package base.encryption.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import base.encryption.beans.Response;
import base.encryption.beans.UserDetail;
import base.encryption.util.DecryptorUtil;


@RestController
@RequestMapping("decrypt")
public class DecryptApi {

	@Autowired
	private DecryptorUtil util;
	
	@RequestMapping(value="/user", method=RequestMethod.POST)
	public ResponseEntity<Response> decryptUser(@RequestBody UserDetail user) {
		
		Response res = new Response();
		res.setMessage("Done");
		res.setStatus(true);
		
		this.util.init(null, user.getField());
		user.setField(null);
		this.util.decryptObjectFields(user);
		res.setData(user);
		
		return new ResponseEntity<Response>(res, HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/user/custom", method=RequestMethod.POST)
	public ResponseEntity<Response> customDecrypt(@RequestBody UserDetail user) {
		
		Response res = new Response();
		res.setMessage("Done");
		res.setStatus(true);
		
		this.util.init(null, user.getField());
		this.util.decryptObjectFields(user);
		
		res.setData(user);
		
		return new ResponseEntity<Response>(res, HttpStatus.CREATED);
	}
}
