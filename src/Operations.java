import java.util.List;

public class Operations<F extends List<Nuplet>>{
	
	F zone;
	Class<F> type;
	int dimention;
	
	Operations(F zone, Class<F> type){
		this.zone = zone;
		this.type = type;
		if(zone.isEmpty())
			dimention = 0;
		else{
			dimention = zone.get(0).getDimention();
		}
	}
	
	public static Nuplet translateForce(Nuplet init, Nuplet vector){
		return init.add(vector);
	}
	
	public F translateForce(F init, Nuplet vector){
		F rtn = null;
		try {
			rtn = type.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		for(Nuplet p : init){
			rtn.add(translateForce(p,vector));
		}
		
		return rtn;
	}
	
	public boolean inZone(Nuplet that){
		return this.zone.contains(that);
	}
	
	public boolean inZone(F that){
		boolean condition = true;
		
		for(Nuplet i : that){
			condition &= this.inZone(i);
		}
		
		return condition;
	}
	
	public F translate(F init, Nuplet vector){
		
		F rtn = null;
		try {
			rtn = type.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		
		for(Nuplet p : init){
			rtn.add(translateForce(p,vector));
			if(!this.inZone(rtn.get(rtn.size()-1))){
				return null;
			}
		}
		
		return rtn;
		
	}
	
	public Nuplet barycenter(F that){
		
		if(that.isEmpty())
			return null;
		
		byte[] buf = new byte[this.dimention];
		for(Nuplet i : that){
			for(int j = 0;j<this.dimention; j++){
				buf[j] += i.get(j);
			}
		}
		
		for(int i = 0; i<this.dimention; i++){
			buf[i] /= that.size();
		}
		
		return new Nuplet(buf);
	}
	
	public F rotateForce(F init, Nuplet vector) throws InstantiationException, IllegalAccessException{
		
		if(this.dimention == 2){
			
			//cas 2D
			
			if(vector.getDimention() != 1){
				System.err.println("Unhandled rotation");
				return null;
			}
			
			Nuplet barycenter = this.barycenter(init);
			F rtn = type.newInstance();
			
			switch(vector.x[0]){
			case 0:
				return init;
			case 1:
				for(Nuplet p : init){
					Nuplet buffer = p.opp(barycenter);
					buffer = new Nuplet(new int[]{-buffer.get(1),buffer.get(0)});
					rtn.add(buffer.add(barycenter));
				}
				return rtn;
			case -1:
				for(Nuplet p : init){
					Nuplet buffer = p.opp(barycenter);
					buffer = new Nuplet(new int[]{buffer.get(1),-buffer.get(0)});
					rtn.add(buffer.add(barycenter));
				}
				return rtn;
			case 2:
				for(Nuplet p : init){
					Nuplet buffer = p.opp(barycenter);
					buffer = new Nuplet(new int[]{-buffer.get(0),-buffer.get(1)});
					rtn.add(buffer.add(barycenter));
				}
				return rtn;
			case -2:
				for(Nuplet p : init){
					Nuplet buffer = p.opp(barycenter);
					buffer = new Nuplet(new int[]{-buffer.get(0),-buffer.get(1)});
					rtn.add(buffer.add(barycenter));
				}
				return rtn;
			default:
				System.err.println("Unhandled rotation");
				return null;
			}
			
		}
		else if(this.dimention == 3){
			
			//cas 3D : 3 angles de rotations, suivant les axes x, y et z.
			
			if(vector.getDimention() != 3){
				System.err.println("Unhandled rotation");
				return null;
			}
			
			Nuplet barycenter = this.barycenter(init);
			F buffer2 = type.newInstance();
			F buffer3 = null;
			
			switch(vector.x[0]){
			case 0:
				buffer2 = init;
				break;
			case 1:
				for(Nuplet p : init){
					Nuplet buffer = p.opp(barycenter);
					buffer = new Nuplet(new int[]{buffer.get(0),-buffer.get(2),buffer.get(1)});
					buffer2.add(buffer.add(barycenter));
				}
				break;
			case -1:
				for(Nuplet p : init){
					Nuplet buffer = p.opp(barycenter);
					buffer = new Nuplet(new int[]{buffer.get(0),buffer.get(2),-buffer.get(1)});
					buffer2.add(buffer.add(barycenter));
				}
				break;
			case 2:
				for(Nuplet p : init){
					Nuplet buffer = p.opp(barycenter);
					buffer = new Nuplet(new int[]{buffer.get(0),-buffer.get(1),-buffer.get(2)});
					buffer2.add(buffer.add(barycenter));
				}
				break;
			case -2:
				for(Nuplet p : init){
					Nuplet buffer = p.opp(barycenter);
					buffer = new Nuplet(new int[]{buffer.get(0),-buffer.get(1),-buffer.get(2)});
					buffer2.add(buffer.add(barycenter));
				}
				break;
			default:
				System.err.println("Unhandled rotation");
				return null;
			}
			
			buffer3 = buffer2;
			buffer2 = type.newInstance();
			
					switch(vector.x[1]){
					case 0:
						buffer2 = buffer3;
						break;
					case 1:
						for(Nuplet p : buffer3){
							Nuplet buffer = p.opp(barycenter);
							buffer = new Nuplet(new int[]{-buffer.get(2),buffer.get(1),buffer.get(0)});
							buffer2.add(buffer.add(barycenter));
						}
						break;
					case -1:
						for(Nuplet p : buffer3){
							Nuplet buffer = p.opp(barycenter);
							buffer = new Nuplet(new int[]{buffer.get(2),buffer.get(1),-buffer.get(0)});
							buffer2.add(buffer.add(barycenter));
						}
						break;
					case 2:
						for(Nuplet p : buffer3){
							Nuplet buffer = p.opp(barycenter);
							buffer = new Nuplet(new int[]{buffer.get(1),-buffer.get(1),-buffer.get(2)});
							buffer2.add(buffer.add(barycenter));
						}
						break;
					case -2:
						for(Nuplet p : buffer3){
							Nuplet buffer = p.opp(barycenter);
							buffer = new Nuplet(new int[]{buffer.get(0),-buffer.get(1),-buffer.get(2)});
							buffer2.add(buffer.add(barycenter));
						}
						break;
					default:
						System.err.println("Unhandled rotation");
						return null;
					}
				
			buffer3 = buffer2;
			buffer2 = type.newInstance();
			
			switch(vector.x[1]){
			case 0:
				buffer2 = buffer3;
				break;
			case 1:
				for(Nuplet p : buffer3){
					Nuplet buffer = p.opp(barycenter);
					buffer = new Nuplet(new int[]{-buffer.get(2),buffer.get(1),buffer.get(0)});
					buffer2.add(buffer.add(barycenter));
				}
				break;
			case -1:
				for(Nuplet p : buffer3){
					Nuplet buffer = p.opp(barycenter);
					buffer = new Nuplet(new int[]{buffer.get(2),buffer.get(1),-buffer.get(0)});
					buffer2.add(buffer.add(barycenter));
				}
				break;
			case 2:
				for(Nuplet p : buffer3){
					Nuplet buffer = p.opp(barycenter);
					buffer = new Nuplet(new int[]{buffer.get(1),-buffer.get(1),-buffer.get(2)});
					buffer2.add(buffer.add(barycenter));
				}
				break;
			case -2:
				for(Nuplet p : buffer3){
					Nuplet buffer = p.opp(barycenter);
					buffer = new Nuplet(new int[]{buffer.get(0),-buffer.get(1),-buffer.get(2)});
					buffer2.add(buffer.add(barycenter));
				}
				break;
			default:
				System.err.println("Unhandled rotation");
				return null;
			}
			
				
		}
		System.err.println("Unhandled rotation");
		return null;
	}
}