## Assignment

Create a search application where you expose an endpoint for a client to search based on a certain radius for tree related data.

- You can find Street tree data from the TreesCount 2015 here => https://data.cityofnewyork.us/Environment/2015-Street-Tree-Census-Tree-Data/uvpi-gqnh
- The direct call to api is `https://data.cityofnewyork.us/resource/nwxe-4ae8.json`
- You have to expose and API endpoint accepting two parameters 
    1. A Cartesian Point specifying a center point along the x & y plane
    2. A search radius in meters

Output
 - You have to retrieve the count of "common name" (please see in the documentation on the same page above which field to refer to) for all 
 the species of trees in that search radius
 - Expected outcome from the api
```json
{
    "red maple": 30,
    "American linden": 1,
    "London planetree": 3
}

```

## Solution
I want to describe some of my decisions and approaches. 
I imagined that many people use our service, so I focused on performance.

### 1. MapStruct
I used MapStruct code generator for mapping objects.
   So I would ask you to enable 'Enable annotation processing'.  
   - Intellij Idea: File -> Settings -> Build, Execution, Deployment -> Compiler -> Annotation Processors - enable checkbox 'Enable annotation processing'.  
   - Eclipse: Properties -> Open Java Compiler -> Annotation Processing. Check "Enable annotation processing". (but I did not check it)


### 2. Double vs BigDecimal?
I have chosen Double as the type for Tree coordinates.
because:
- BigDecimal is more suitable for situations where precision is really important, for example for money operations or some scientific calculating.
In our situation, I don`t think that precision by millimeters is important.
- Operations with BigDecimal is slower than with Double.

### 3. Cache for Rest endpoint? 
I believe that cache is not needed here.
Because we have huge number of coordinates and radius, the probability that somebody write same values is little.
Cache is more suitable if there are a lot of same inputs.

### 4. Algorithms for searching.

#### 4.1 Simple Traverse Algorithm (STA).
It traverses all points and checks the distance from a point to the center.

#### 4.2 Cutoff Algorithm (CA)
I have invented a new Algorithm and called it Cutoff Algorithm.
It is much faster than the Simple Traverse Algorithm.

##### 4.2.1 Given data for CA.
- We have a list of trees.  
- Tree has coordinates x and y.  
- xc and yc - x and y coordinate of the center respectively.  
- r - radius of the circle.  
- Real data from Census:  
    xmax - xmin ~ 40000 meters  
    ymax - ymin ~ 40000 meters  

We can be sure in next points:
- If a tree has x coordinate satisfy condition (x < xc - r) || (x > xc + r) => this tree doesn`t belong the circle.
- If a tree has x coordinate satisfy condition (x >= xc - r) && (x <= xc + r) => this tree may or may not belong the circle.
- If a tree has y coordinate satisfy condition (y < yc - r) || (y > yc + r) => this tree doesn`t belong the circle
- If a tree has y coordinate satisfy condition (y >= yc - r) && (y <= yc + r) => this tree may or may not belong the circle.
We can imagine square around the circle and condition above checks whether a point belongs to the square.

##### 4.2.2 Description of the CA.
1) sort tree list by x coordinate ascending (T1).
2) find all points which satisfy condition (x >= xc - r) && (x <= xc + r).
And here is a trick. We can do it by sorted tree list and binary search.  
So we find the index of the first tree with x >= xc - r and do it by log N iterations.  
And find the index of the last tree with x <= xc + r and do it by log N iterations.  
So we quickly get the list of trees T2. All trees in T2 have x coordinates between xc - r and xc + r.  
For r << xmax - xmin , size T2 << T1
3) Traverse by T2 tree list and find only trees with y coordinates between yc - r and yc + r.
So we have list T3. There are all trees enclosed in the square around the circle.
size T3 < T2  
4) Traverse by T3, check the distance to the center and get the result.

#### 4.3 Comparison of CA and STA.
I implemented a benchmark/statistics test and compared two algorithms.
You can find it here com.holidu.interview.assignment.service.TreeDataSearchServicePerfomanceBenchmark
in the benchmark directory.

##### 4.3.1 Given data.
  
- There is a plot 100_000 x 100_000 meters (MAX_X x MAX_Y).  
- Center is (50_000;50_000) (CENTER_X;CENTER_Y)  
- There is a list of tree counts 1_000, 10_000, 20_000, 100_000 (TREE_COUNT_LIST).  
- There is a list of radiuses 10d, 100d, 1_000d, 10_000d, 25_000d, 40_000d, 50_000d, 75_000d, 100_000d (RADIUS_LIST).  

I compared two algorithms for every situation.
Every experiment runs 10_000 times (MAX_EXP).

##### 4.3.2 Comparison results.
```
There is results:
Tree count = 1000:
radius:                 	10.0      	100.0     	1000.0    	10000.0   	25000.0   	40000.0   	50000.0   	75000.0   	100000.0  	
cutoffAlgorithm (sec):  	0.004     	0.005     	0.007     	0.029     	0.048     	0.094     	0.107     	0.219     	0.169     	
traverseAlgorithm (sec):	0.108     	0.112     	0.109     	0.122     	0.09      	0.107     	0.112     	0.15      	0.159     	

Tree count = 10000:
radius:                 	10.0      	100.0     	1000.0    	10000.0   	25000.0   	40000.0   	50000.0   	75000.0   	100000.0  	
cutoffAlgorithm (sec):  	0.003     	0.004     	0.014     	0.248     	1.074     	2.926     	1.695     	1.67      	1.55      	
traverseAlgorithm (sec):	1.353     	1.256     	1.653     	1.266     	3.403     	2.147     	1.779     	1.51      	1.454     	

Tree count = 20000:
radius:                 	10.0      	100.0     	1000.0    	10000.0   	25000.0   	40000.0   	50000.0   	75000.0   	100000.0  	
cutoffAlgorithm (sec):  	0.003     	0.005     	0.03      	0.416     	1.86      	3.158     	3.646     	2.822     	2.836     	
traverseAlgorithm (sec):	2.825     	2.775     	2.773     	2.482     	3.162     	3.531     	3.415     	2.801     	2.833     	

Tree count = 100000:
radius:                 	10.0      	100.0     	1000.0    	10000.0   	25000.0   	40000.0   	50000.0   	75000.0   	100000.0  	
cutoffAlgorithm (sec):  	0.004     	0.014     	0.206     	3.861     	14.205    	25.047    	29.412    	26.467    	26.323    	
traverseAlgorithm (sec):	36.995    	37.557    	37.485    	21.5      	25.467    	29.07     	28.277    	27.029    	27.335 
```

We can see:
- if the radius < MAX_X/2 (50_000) , Cuttof algorithm is faster than Simple Traverse Algorithm.  
For example, radius 1000,  
trees = 1000, CA is faster than STA in 0.109/0.07 = 15.6 times  
trees = 100_000, CA is faster than STA in 37.485/0.206 = 182 times
 
- if the radius >= MAX_X/2 (50_000), Cuttoff algorithm and Simple Traverse Algorithm have the same speed.  
It is correct. When xc - r = X_MIN and  xc + r = X_MAX Cutoff Algorithm in 4.2.2 point 2 finds all trees and does not decrease the tree list (T2=T1).  
But I believe it is a rare situation that somebody input so big radius.  

So in general situation, CA is better than STA.

### 5. Endpoint
I exposed the rest endpoint here
```
http://localhost:8080/holidu-test-task/tree/count?coord=COORD_X;COORD_Y&radius=RADIUS
```  
You said that the radius unit is meter. Therefore I decided that all coordinates must be in meters also.

example of call:
```  
http://localhost:8080/holidu-test-task/tree/count?coord=313164;61800&radius=1000
```
